package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.chat.Message;

import java.util.Collection;

public interface MessageService {
    Message createMessage(Message message);
    Message getMessageById(Long messageId);
    Collection<Message> getMessages(Long chatId, int page, int size);
}
