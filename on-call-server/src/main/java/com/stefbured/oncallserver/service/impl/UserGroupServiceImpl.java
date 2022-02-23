package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.dto.UserGroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.UserGroup;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.UserGroupRepository;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserGroupServiceImpl(UserGroupRepository userGroupRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserGroupDTO getByGroupSequence(String[] pathPieces) {
        var currentGroup = userGroupRepository.getById(0L);
        var parentGroup = currentGroup;
        for (var pathPiece : pathPieces) {
            parentGroup = currentGroup;
            currentGroup = userGroupRepository.getByParentGroupAndIdTag(currentGroup, pathPiece);
        }
        return UserGroupDTO.builder()
                .id(currentGroup.getId())
                .idTag(currentGroup.getIdTag())
                .name(currentGroup.getName())
                .description(currentGroup.getDescription())
                .creationDate(currentGroup.getCreationDate())
                .creator(convertUserToUserDTO(currentGroup.getCreator()))
                .owner(convertUserToUserDTO(currentGroup.getOwner()))
                .parentGroup(convertUserGroupToUserGroupDTO(parentGroup))
                .build();
    }

    @Override
    public Set<UserDTO> getGroupMembers(Long groupId, int page, int pageSize) {
        var group = userGroupRepository.getById(groupId);
        return getGroupMembers(group, page, pageSize);
    }

    private Set<UserDTO> getGroupMembers(UserGroup group, int page, int pageSize) {
        var members = userRepository.findAllByUserGroupsContains(group, Pageable.ofSize(pageSize).withPage(page));
        return members.stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toSet());
    }

    private UserGroupDTO convertUserGroupToUserGroupDTO(UserGroup userGroup) {
        return UserGroupDTO.builder()
                .id(userGroup.getId())
                .idTag(userGroup.getIdTag())
                .name(userGroup.getName())
                .build();
    }

    private UserDTO convertUserToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
