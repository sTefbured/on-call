package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.role.UserGrant;

public interface UserGrantService {
    UserGrant createUserGrant(UserGrant userGrant);
    UserGrant getUserGrantById(Long userGrantId);
    void deleteUserGrantById(Long userGrantId);
}
