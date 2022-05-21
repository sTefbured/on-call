package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.exception.EmptyMessageException;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.dto.chat.MessageDTO;
import com.stefbured.oncallserver.model.dto.validation.ViolationDTO;
import com.stefbured.oncallserver.model.entity.chat.Message;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.ChatService;
import com.stefbured.oncallserver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.naming.LimitExceededException;
import java.util.Collection;

import static com.stefbured.oncallserver.OnCallConstants.MESSAGE_SEND;
import static com.stefbured.oncallserver.OnCallConstants.MESSAGE_VIEW;
import static com.stefbured.oncallserver.config.OnCallPermissionEvaluator.CHAT_TARGET_TYPE;
import static com.stefbured.oncallserver.mapper.MessageModelMapper.MESSAGE_MODEL_MAPPER;
import static com.stefbured.oncallserver.mapper.MessageModelMapper.MESSAGE_TO_FULL_DTO;

@RestController
@RequestMapping("api/v1/message")
public class MessageController {
    private final MessageService messageService;
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final OnCallModelMapper messageMapper;

    @Autowired
    public MessageController(MessageService messageService,
                             ChatService chatService,
                             SimpMessagingTemplate simpMessagingTemplate,
                             @Qualifier(MESSAGE_MODEL_MAPPER) OnCallModelMapper messageMapper) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageMapper = messageMapper;
    }

    @PostMapping
    @PreAuthorize("hasPermission(#message.chat.id, '" + CHAT_TARGET_TYPE + "', '" + MESSAGE_SEND + "')")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO message) {
        var messageEntity = new Message();
        messageMapper.mapSkippingNullValues(message, messageEntity);
        var sender = new User();
        var senderId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        sender.setId(senderId);
        messageEntity.setSender(sender);
        var createdMessage = messageService.createMessage(messageEntity);
        var result = messageMapper.map(createdMessage, MessageDTO.class, MESSAGE_TO_FULL_DTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{messageId}")
    @PostAuthorize("hasPermission(returnObject.body.chat?.id, '" + CHAT_TARGET_TYPE + "', '" + MESSAGE_VIEW + "')")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable Long messageId) {
        var queriedMessage = messageService.getMessageById(messageId);
        var result = messageMapper.map(queriedMessage, MessageDTO.class, MESSAGE_TO_FULL_DTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("all")
    @PreAuthorize("hasPermission(#chatId, '" + CHAT_TARGET_TYPE + "', '" + MESSAGE_VIEW + "')")
    public ResponseEntity<Collection<MessageDTO>> getMessages(@RequestParam Long chatId,
                                                              @RequestParam int page,
                                                              @RequestParam int pageSize) {
        Collection<Message> messages;
        try {
            messages = messageService.getMessages(chatId, page, pageSize);
        } catch (LimitExceededException exception) {

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        var result = messageMapper.mapCollection(messages, MessageDTO.class, MESSAGE_TO_FULL_DTO);
        return ResponseEntity.ok(result);
    }


    // Realtime messaging

    @MessageMapping("/message")
    @Transactional
    public MessageDTO receiveMessage(@Payload MessageDTO message) {
        var messageEntity = new Message();
        messageMapper.mapSkippingNullValues(message, messageEntity);
        Message createdMessage;
        try {
            createdMessage = messageService.createMessage(messageEntity);
        } catch (EmptyMessageException exception) {
            var senderId = message.getSender().getId().toString();
            var errorData = new ViolationDTO("text", exception.getMessage());
            simpMessagingTemplate.convertAndSendToUser(senderId, "/message", errorData);
            return null;
        }
        var result = messageMapper.map(createdMessage, MessageDTO.class, MESSAGE_TO_FULL_DTO);
        var userIds = chatService.getAllChatMembersIds(createdMessage.getChat());
        for (var userId : userIds) {
            simpMessagingTemplate.convertAndSendToUser(userId.toString(), "/message", result);
        }
        return result;
    }
}
