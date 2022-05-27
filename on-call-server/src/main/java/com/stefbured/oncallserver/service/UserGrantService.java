package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.role.UserGrant;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface UserGrantService {
    UserGrant createUserGrant(UserGrant userGrant);
    UserGrant getUserGrantById(Long userGrantId);
    Page<UserGrant> getAllGroupUserGrantsForUser(Long userId, int page, int pageSize);
    void deleteUserGrantById(Long userGrantId);
    void deleteUserGrantByChatIdAndUserId(Long chatId, Long userId);
}
