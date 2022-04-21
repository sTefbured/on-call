package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.user.User;

import java.util.Set;

public interface UserService {
    User register(User user);
    User getUserByUsername(String username);
    User getUserById(Long id);
    Set<User> getUsers(int page, int pageSize);
    long getUsersCount();
    User updateUser(User user);
    void deleteUserById(Long userId);
    boolean userHasAnyAuthority(String username, String... authorities);
    boolean userHasGlobalAuthority(String username, String authority);
    boolean userHasAuthorityForGroup(String username, Long groupId, String authority);
    boolean userHasAuthorityForChat(String username, Long chatId, String authority);
    Long getUserIdByUsername(String username);
}
