package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.user.User;

import java.util.Set;

public interface UserService {
    User register(User user);
    User registerWithRoles(User user, Set<Long> rolesIds);
    User getUserByUsername(String username);
    User getUserById(Long id);
    User updateUser(User user);
    void deleteUserById(Long userId);
    boolean userHasAnyAuthority(String username, String... authorities);
    boolean userHasGlobalAuthority(String username, String authority);
    boolean userHasAuthorityForGroup(String username, Long groupId, String authority);
    Long getUserIdByUsername(String username);
}
