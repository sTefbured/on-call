package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.dto.user.UserRegisterDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.user.rights.Role;

import java.util.Set;

public interface UserService {
    User register(UserRegisterDTO user);
    User registerWithRoles(UserRegisterDTO user, Set<Role> roles);
    User getByUsername(String username);
}
