package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.chat.Chat;

public interface ChatService {
    Chat createChat(Chat chat);
    Chat getChatById(Long chatId);
    Chat updateChat(Chat chat);
    void deleteChatById(Long chatId);
}
