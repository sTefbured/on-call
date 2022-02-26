package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.dto.user.UserDTO;

import java.util.Set;

public interface UserService {
    UserDTO register(UserDTO user);
    UserDTO registerWithRoles(UserDTO user, Set<Long> rolesIds);
    UserDTO getByUsername(String username);
    Long getUserIdByUsername(String username);
}
