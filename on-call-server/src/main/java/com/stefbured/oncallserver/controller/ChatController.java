package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.config.OnCallPermissionEvaluator;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.dto.chat.ChatDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.role.Role;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.ChatService;
import com.stefbured.oncallserver.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static com.stefbured.oncallserver.OnCallConstants.*;
import static com.stefbured.oncallserver.config.OnCallPermissionEvaluator.*;
import static com.stefbured.oncallserver.mapper.ChatModelMapper.*;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_MODEL_MAPPER;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PREVIEW_DTO;

@RestController
@RequestMapping("api/v1/chat")
public class ChatController {
    private final ChatService chatService;
    private final GroupService groupService;
    private final OnCallModelMapper chatModelMapper;
    private final OnCallModelMapper userModelMapper;

    @Autowired
    public ChatController(ChatService chatService,
                          GroupService groupService,
                          @Qualifier(CHAT_MODEL_MAPPER) OnCallModelMapper chatModelMapper,
                          @Qualifier(USER_MODEL_MAPPER) OnCallModelMapper userModelMapper) {
        this.chatService = chatService;
        this.groupService = groupService;
        this.chatModelMapper = chatModelMapper;
        this.userModelMapper = userModelMapper;
    }

    @PostMapping
    @PreAuthorize("(#chat.group == null && hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + CHAT_CREATE + "')) " +
            "|| hasPermission(#chat.group?.id, '" + GROUP_TARGET_TYPE + "', '" + GROUP_CHAT_CREATE + "') " +
            "|| (#chat.group != null && hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + GROUP_CHAT_CREATE + "'))")
    public ResponseEntity<ChatDTO> createChat(@RequestBody ChatDTO chat, HttpServletRequest request) {
        var chatEntity = new Chat();
        chatModelMapper.mapSkippingNullValues(chat, chatEntity);
        var user = new User();
        var userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        user.setId(userId);
        chatEntity.setCreator(user);
        var createdChat = chatService.createChat(chatEntity);
        var result = chatModelMapper.map(createdChat, ChatDTO.class, CHAT_TO_VIEW_DTO);
        var locationUri = URI.create(request.getRequestURI()).resolve(result.getId().toString());
        return ResponseEntity.created(locationUri).body(result);
    }

    @PostMapping("{chatId}/member/{userId}")
    @PreAuthorize("hasPermission(#chatId, '" + CHAT_TARGET_TYPE + "', '" + CHAT_ADD_MEMBER + "')")
    public ResponseEntity<UserDTO> addMember(@PathVariable Long chatId, @PathVariable Long userId) {
        var addedMember = chatService.addMemberById(chatId, userId);
        var result = userModelMapper.map(addedMember, UserDTO.class, USER_TO_PREVIEW_DTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{chatId}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable Long chatId) {
        var queriedChat = chatService.getChatById(chatId);
        var userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if ((queriedChat.getGroup() != null && groupService.isUserMemberOfGroup(userId, queriedChat.getGroup().getId()))
                || OnCallPermissionEvaluator.hasPermission(chatId, CHAT_TARGET_TYPE, CHAT_VIEW)
                || OnCallPermissionEvaluator.hasPermission((Long) null, GLOBAL_TARGET_TYPE, CHAT_VIEW)) {
            var result = chatModelMapper.map(queriedChat, ChatDTO.class, CHAT_TO_VIEW_DTO);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("user/{userId}")
    @PreAuthorize("#userId.equals(authentication.details)")
    public ResponseEntity<Collection<ChatDTO>> getUserChats(@PathVariable Long userId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int pageSize) {
        var queriedChats = chatService.getAllForUser(userId, page, pageSize);
        var chatsList = queriedChats.toList();
        var result = chatModelMapper.mapCollection(chatsList, ChatDTO.class, CHAT_TO_VIEW_DTO);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, String.valueOf(queriedChats.getTotalElements()))
                .body(result);
    }

    @GetMapping("group/{groupId}")
    @PreAuthorize("hasPermission(#groupId, '" + GROUP_TARGET_TYPE + "', '" + GROUP_MEMBER_VIEW + "')")
    public ResponseEntity<Collection<ChatDTO>> getGroupChats(@PathVariable Long groupId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int pageSize) {
        var queriedChats = chatService.getAllForGroup(groupId, page, pageSize);
        var chatsList = queriedChats.toList();
        var result = chatModelMapper.mapCollection(chatsList, ChatDTO.class, CHAT_TO_VIEW_DTO);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, String.valueOf(queriedChats.getTotalElements()))
                .body(result);
    }

    @PostMapping("user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatDTO> findOrCreateChat(@RequestBody UserDTO targetUser) {
        var userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Chat chat;
        try {
            chat = chatService.findPrivateDialog(userId, targetUser.getId());
        } catch (NoSuchElementException exception) {
            var newChat = new Chat();
            var user = new User();
            user.setId(userId);
            newChat.setCreator(user);
            var targetUserEntity = new User();
            targetUserEntity.setId(targetUser.getId());
            var targetUserGrant = new UserGrant();
            targetUserGrant.setUser(targetUserEntity);
            var role = new Role();
            role.setId(CHAT_MEMBER);
            targetUserGrant.setRole(role);
            var grantsSet = new HashSet<UserGrant>();
            grantsSet.add(targetUserGrant);
            newChat.setUsersGrants(grantsSet);
            newChat.setName("Dialog");
            chat = chatService.createChat(newChat);
        }
        var result = chatModelMapper.map(chat, ChatDTO.class, CHAT_TO_ID_DTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    @PreAuthorize("hasPermission(#chat.id, '" + CHAT_TARGET_TYPE + "', '" + CHAT_EDIT + "') " +
            "|| hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + CHAT_EDIT + "')")
    public ResponseEntity<ChatDTO> editChat(@RequestBody ChatDTO chat) {
        var chatEntity = chatService.getChatById(chat.getId());
        chat.setUsersGrants(null);
        chat.setCreator(null);
        chat.setCreationDateTime(null);
        chat.setGroup(null);
        chat.setMessages(null);
        chatModelMapper.mapSkippingNullValues(chat, chatEntity);
        var updatedChat = chatService.updateChat(chatEntity);
        var result = chatModelMapper.map(updatedChat, ChatDTO.class, CHAT_TO_VIEW_DTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{chatId}")
    @PreAuthorize("hasPermission(#chatId, '" + CHAT_TARGET_TYPE + "', '" + CHAT_DELETE + "') " +
            "|| hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + CHAT_DELETE + "')")
    public void deleteChatById(@PathVariable Long chatId) {
        chatService.deleteChatById(chatId);
    }

    @DeleteMapping("{chatId}/member/{userId}")
    @PreAuthorize("authentication.details.equals(#userId) " +
            "|| hasPermission(#chatId, '" + CHAT_TARGET_TYPE + "', '" + CHAT_REMOVE_MEMBER + "')")
    public void removeUserFromChat(@PathVariable Long chatId, @PathVariable Long userId) {
        chatService.removeMemberById(chatId, userId);
    }
}
