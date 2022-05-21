package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.role.Role;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.ChatRepository;
import com.stefbured.oncallserver.service.ChatService;
import com.stefbured.oncallserver.service.GroupService;
import com.stefbured.oncallserver.service.UserGrantService;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

import static com.stefbured.oncallserver.OnCallConstants.CHAT_ADMINISTRATOR;
import static com.stefbured.oncallserver.OnCallConstants.CHAT_MEMBER;

@Service
public class ChatServiceImpl implements ChatService {
    private final UserGrantService userGrantService;
    private final UserService userService;
    private final GroupService groupService;
    private final ChatRepository chatRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public ChatServiceImpl(UserGrantService userGrantService,
                           GroupService groupService,
                           ChatRepository chatRepository,
                           UserService userService,
                           LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.userGrantService = userGrantService;
        this.groupService = groupService;
        this.chatRepository = chatRepository;
        this.userService = userService;
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Override
    public Chat createChat(Chat chat) {
        chat.setId(primaryKeyGenerator.generatePk(Chat.class));
        chat.setCreationDateTime(LocalDateTime.now());
        var createdChat = chatRepository.save(chat);
        var userGrant = new UserGrant();
        userGrant.setUser(createdChat.getCreator());
        var role = new Role();
        role.setId(CHAT_ADMINISTRATOR);
        userGrant.setRole(role);
        userGrant.setChat(createdChat);
        userGrantService.createUserGrant(userGrant);
        return createdChat;
    }

    @Override
    public User addMemberById(Long chatId, Long userId) {
        var chat = chatRepository.findById(chatId).orElseThrow();
        var userGrant = new UserGrant();
        userGrant.setChat(chat);
        var role = new Role();
        role.setId(CHAT_MEMBER);
        userGrant.setRole(role);
        var user = userService.getUserById(userId);
        userGrant.setUser(user);
        userGrantService.createUserGrant(userGrant);
        return user;
    }

    @Override
    public Chat getChatById(Long chatId) {
        return chatRepository.getById(chatId);
    }

    @Override
    public Collection<Chat> getAllForUser(Long userId, int page, int pageSize) {
        return chatRepository.findAllByUserId(userId, Pageable.ofSize(pageSize).withPage(page)).toList();
    }

    @Override
    public Collection<Chat> getAllForGroup(Long groupId, int page, int pageSize) {
        return chatRepository.findAllByGroupId(groupId, Pageable.ofSize(pageSize).withPage(page)).toList();
    }

    @Override
    public Collection<Long> getAllChatMembersIds(Chat chat) {
        if (chat.getGroup() == null) {
            return chatRepository.findAllUserChatMembersIds(chat.getId());
        }
        var groupId = chat.getGroup().getId();
        var membersCount = groupService.getGroupMembersCount(groupId);
        return groupService.getGroupMembers(groupId, 0, membersCount.intValue()).stream().map(User::getId).toList(); //FIXME not cool
    }

    @Override
    public Chat updateChat(Chat chat) {
        if (!chatRepository.existsById(chat.getId())) {
            throw new IllegalArgumentException();
        }
        return chatRepository.save(chat);
    }

    @Override
    public void deleteChatById(Long chatId) {
        if (!chatRepository.existsById(chatId)) {
            throw new NullPointerException("Chat with id '" + chatId + "' doesn't exist");
        }
        chatRepository.deleteById(chatId);
    }

    @Override
    public void removeMemberById(Long chatId, Long userId) {
        userGrantService.deleteUserGrantByChatIdAndUserId(chatId, userId);
    }
}
