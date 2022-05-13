package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.user.User;

import java.util.Collection;

public interface UserService {
    User register(User user);
    User getUserByUsername(String username);
    User getUserById(Long id);
    Collection<User> getUsers(int page, int pageSize);
    long getUsersCount();
    User updateUser(User user);
    boolean userHasGlobalAuthority(Long userId, String authority);
    boolean userHasAuthorityForGroup(Long userId, Long groupId, String authority);
    boolean userHasAuthorityForChat(Long userId, Long chatId, String authority);
    Long getUserIdByUsername(String username);
    Collection<String> getAuthorityNamesForUserId(Long id);
    boolean isUserExists(Long userId);
}
