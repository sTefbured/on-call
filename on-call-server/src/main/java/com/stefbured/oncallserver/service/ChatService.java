package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.user.User;

public interface ChatService {
    Chat createChat(Chat chat);
    User addMemberById(Long chatId, Long userId);
    Chat getChatById(Long chatId);
    Chat updateChat(Chat chat);
    void deleteChatById(Long chatId);
    void removeMemberById(Long chatId, Long userId);
}
