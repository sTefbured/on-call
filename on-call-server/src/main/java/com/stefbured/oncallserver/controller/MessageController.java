package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.dto.chat.MessageDTO;
import com.stefbured.oncallserver.model.entity.chat.Message;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    private final OnCallModelMapper messageMapper;

    @Autowired
    public MessageController(MessageService messageService,
                             @Qualifier(MESSAGE_MODEL_MAPPER) OnCallModelMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @PostMapping
    @PreAuthorize("hasPermission(#message.chat, '" + CHAT_TARGET_TYPE + "', '" + MESSAGE_SEND + "')")
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
        var messages = messageService.getMessages(chatId, page, pageSize);
        var result = messageMapper.mapCollection(messages, MessageDTO.class, MESSAGE_TO_FULL_DTO);
        return ResponseEntity.ok(result);
    }
}
