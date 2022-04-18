package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.user.User;

import java.util.Set;

public interface GroupService {
    Group getById(Long groupId);
    Group getByGroupSequence(String[] pathPieces);
    Set<User> getGroupMembers(Long groupId, int page, int pageSize);
    Group create(Group group);
    Group update(Group group);
    void delete(Long groupId);
}
