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
    @Deprecated
    void deleteUserById(Long userId);
    @Deprecated
    boolean userHasAnyAuthority(String username, String... authorities);
    @Deprecated
    boolean userHasGlobalAuthority(String username, String authority);
    boolean userHasGlobalAuthority(Long userId, String authority);
    @Deprecated
    boolean userHasAuthorityForGroup(String username, Long groupId, String authority);
    boolean userHasAuthorityForGroup(Long userId, Long groupId, String authority);
    @Deprecated
    boolean userHasAuthorityForChat(String username, Long chatId, String authority);
    boolean userHasAuthorityForChat(Long userId, Long chatId, String authority);
    Long getUserIdByUsername(String username);
}
