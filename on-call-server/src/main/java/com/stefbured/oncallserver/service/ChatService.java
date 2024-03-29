package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.user.User;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ChatService {
    Chat createChat(Chat chat);
    User addMemberById(Long chatId, Long userId);
    Chat getChatById(Long chatId);
    Page<Chat> getAllForUser(Long userId, int page, int pageSize);
    Page<Chat> getAllForGroup(Long groupId, int page, int pageSize);
    Collection<Long> getAllChatMembersIds(Chat chat);
    Chat findPrivateDialog(Long firstUserId, Long secondUserId);
    Chat updateChat(Chat chat);
    void deleteChatById(Long chatId);
    void removeMemberById(Long chatId, Long userId);
}
