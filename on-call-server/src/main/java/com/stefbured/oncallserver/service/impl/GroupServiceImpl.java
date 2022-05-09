package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.GroupNotFoundException;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.GroupRepository;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.GroupService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            UserRepository userRepository,
                            LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Override
    public Collection<Group> getFirstLevelGroups(int page, int pageSize) {
        return groupRepository.findAllFirstLevel(Pageable.ofSize(pageSize).withPage(page)).toList();
    }

    @Override
    public long getFirstLevelGroupsCount() {
        return groupRepository.findAllFirstLevelCount();
    }

    @Override
    public Group getById(Long groupId) {
        var group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        var currentGroup = group;
        var fullIdTagBuilder = new StringBuilder();
        do {
            fullIdTagBuilder.insert(0, '/' + currentGroup.getIdTag());
            currentGroup = currentGroup.getParentGroup();
        } while (currentGroup != null);
        group.setIdTag(fullIdTagBuilder.toString());
        return group;
    }

    @Override
    public Group getByGroupSequence(String[] pathPieces) {
        Group currentGroup = null;
        StringBuilder fullIdTagBuilder = new StringBuilder();
        for (var pathPiece : pathPieces) {
            currentGroup = groupRepository.findByParentGroupAndIdTag(currentGroup, pathPiece)
                    .orElseThrow(GroupNotFoundException::new);
            fullIdTagBuilder.append("/").append(currentGroup.getIdTag());
        }
        if (currentGroup == null) {
            throw new GroupNotFoundException();
        }
        currentGroup.setIdTag(fullIdTagBuilder.toString());
        return currentGroup;
    }

    @Override
    public Set<User> getGroupMembers(Long groupId, int page, int pageSize) {
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException();
        }
        return userRepository.getGroupMembers(groupId, Pageable.ofSize(pageSize).withPage(page)).toSet();
    }

    @Override
    public Long getGroupMembersCount(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException();
        }
        return userRepository.getGroupMembersCount(groupId);
    }

    @Override
    public Group create(Group group) {
        var primaryKey = primaryKeyGenerator.generatePk(Group.class);
        group.setId(primaryKey);
        group.setCreationDateTime(LocalDateTime.now());
        group.setChildGroups(null);
        group.setScheduleRecords(null);
        group.setChats(null);
        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group) {
        if (!groupRepository.existsById(group.getId())) {
            throw new GroupNotFoundException();
        }
        return groupRepository.save(group);
    }

    @Override
    public void delete(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException();
        }
        groupRepository.deleteById(groupId);
    }
}
