package com.stefbured.oncallserver.service.user;

import com.stefbured.oncallserver.model.dto.user.UserDetailsDTO;
import com.stefbured.oncallserver.model.dto.user.UserRegisterDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.user.rights.Role;

import java.util.Set;

public interface UserService {
    UserDetailsDTO register(UserRegisterDTO user);
    UserDetailsDTO registerWithRoles(UserRegisterDTO user, Set<Role> roles);
    User getByUsername(String username);
}
