package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.chat.Message;
import com.stefbured.oncallserver.repository.MessageRepository;
import com.stefbured.oncallserver.service.MessageService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.messageRepository = messageRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Override
    public Message createMessage(Message message) {
        message.setId(primaryKeyGenerator.generatePk(Message.class));
        message.setSendingDateTime(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public Message getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow();
    }

    @Override
    public Collection<Message> getMessages(Long chatId, int page, int size) {
        return messageRepository.getAllByChatId(chatId, Pageable.ofSize(size).withPage(page));
    }
}
