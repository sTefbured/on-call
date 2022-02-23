package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.dto.UserGroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;

import java.util.Set;

public interface UserGroupService {
    UserGroupDTO getByGroupSequence(String[] pathPieces);
    Set<UserDTO> getGroupMembers(Long groupId, int page, int pageSize);
}
