package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.role.Role;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.repository.ChatRepository;
import com.stefbured.oncallserver.service.ChatService;
import com.stefbured.oncallserver.service.UserGrantService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.stefbured.oncallserver.OnCallConstants.CHAT_ADMINISTRATOR;

@Service
public class ChatServiceImpl implements ChatService {
    private final UserGrantService userGrantService;
    private final ChatRepository chatRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public ChatServiceImpl(UserGrantService userGrantService,
                           ChatRepository chatRepository,
                           LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.userGrantService = userGrantService;
        this.chatRepository = chatRepository;
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
    public Chat getChatById(Long chatId) {
        return chatRepository.getById(chatId);
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
}
