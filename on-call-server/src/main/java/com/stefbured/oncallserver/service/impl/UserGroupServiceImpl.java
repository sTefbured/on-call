package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.dto.UserGroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.UserGroup;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.UserGroupRepository;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.UserGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static com.stefbured.oncallserver.config.ModelMapperConfiguration.USER_GROUP_TO_DTO;
import static com.stefbured.oncallserver.config.ModelMapperConfiguration.USER_TO_LIMITED_DTO;

@Service
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserGroupServiceImpl(UserGroupRepository userGroupRepository,
                                UserRepository userRepository,
                                ModelMapper modelMapper) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserGroupDTO getByGroupSequence(String[] pathPieces) {
        var currentGroup = userGroupRepository.getById(0L);
        for (var pathPiece : pathPieces) {
            currentGroup = userGroupRepository.getByParentGroupAndIdTag(currentGroup, pathPiece);
        }
        return modelMapper.getTypeMap(UserGroup.class, UserGroupDTO.class, USER_GROUP_TO_DTO).map(currentGroup);
    }

    @Override
    public Set<UserDTO> getGroupMembers(Long groupId, int page, int pageSize) {
        var group = userGroupRepository.getById(groupId);
        var members = userRepository.findAllByUserGroupsContains(group, Pageable.ofSize(pageSize).withPage(page));
        var userMapper = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_LIMITED_DTO);
        return members.stream()
                .map(userMapper::map)
                .collect(Collectors.toSet());
    }
}
