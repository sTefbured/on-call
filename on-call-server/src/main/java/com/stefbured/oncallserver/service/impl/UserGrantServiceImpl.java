package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.repository.UserGrantRepository;
import com.stefbured.oncallserver.service.UserGrantService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserGrantServiceImpl implements UserGrantService {
    private final UserGrantRepository userGrantRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public UserGrantServiceImpl(UserGrantRepository userGrantRepository, LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.userGrantRepository = userGrantRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Override
    public UserGrant createUserGrant(UserGrant userGrant) {
        userGrant.setId(primaryKeyGenerator.generatePk(UserGrant.class));
        return userGrantRepository.save(userGrant);
    }

    @Override
    public UserGrant getUserGrantById(Long userGrantId) {
        return userGrantRepository.findById(userGrantId).orElseThrow();
    }

    @Override
    public Page<UserGrant> getAllGroupUserGrantsForUser(Long userId, int page, int pageSize) {
        var pageable = Pageable.ofSize(pageSize).withPage(page);
        return userGrantRepository.findAllByUserIdAndGroupIsNotNull(userId, pageable);
    }

    @Override
    public void deleteUserGrantById(Long userGrantId) {
        userGrantRepository.deleteById(userGrantId);
    }

    @Override
    public void deleteUserGrantByChatIdAndUserId(Long chatId, Long userId) {
        userGrantRepository.deleteByChatIdAndUserId(chatId, userId);
    }
}
