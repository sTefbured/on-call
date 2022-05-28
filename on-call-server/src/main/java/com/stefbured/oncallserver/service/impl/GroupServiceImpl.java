package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.GroupNotFoundException;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.group.JoinGroupRequest;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.GroupRepository;
import com.stefbured.oncallserver.repository.JoinGroupRequestRepository;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.GroupService;
import com.stefbured.oncallserver.service.NotificationService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static com.stefbured.oncallserver.OnCallConstants.GROUP_ADD_MEMBER;
import static com.stefbured.oncallserver.OnCallConstants.NotificationTypes.JOIN_GROUP_REQUEST;

@Service
public class GroupServiceImpl implements GroupService {
    private final NotificationService notificationService;
    private final GroupRepository groupRepository;
    private final JoinGroupRequestRepository joinGroupRequestRepository;
    private final UserRepository userRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public GroupServiceImpl(NotificationService notificationService,
                            GroupRepository groupRepository,
                            JoinGroupRequestRepository joinGroupRequestRepository,
                            UserRepository userRepository,
                            LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.notificationService = notificationService;
        this.groupRepository = groupRepository;
        this.joinGroupRequestRepository = joinGroupRequestRepository;
        this.userRepository = userRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Override
    public Collection<Group> getFirstLevelGroups(int page, int pageSize) {
        return groupRepository.findAllFirstLevel(Pageable.ofSize(pageSize).withPage(page))
                .map(group -> {
                    group.setIdTag("/" + group.getIdTag());
                    return group;
                }).toList();
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
        var detachedGroup = getGroupCopy(group);
        detachedGroup.setIdTag(fullIdTagBuilder.toString());
        return detachedGroup;
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
    public Collection<JoinGroupRequest> getJoinRequests(Long groupId, int page, int pageSize) {
        return joinGroupRequestRepository.findAllByGroupId(groupId, Pageable.ofSize(pageSize).withPage(page)).toList();
    }

    @Override
    public long getJoinRequestsCount(Long groupId) {
        return joinGroupRequestRepository.countAllByGroupId(groupId);
    }

    @Override
    public JoinGroupRequest createJoinRequest(JoinGroupRequest request) {
        var creator = request.getUser();
        if (request.getUser() == null) {
            throw new NullPointerException();
        }
        request.setId(primaryKeyGenerator.generatePk(JoinGroupRequest.class));
        request.setCreationDate(LocalDateTime.now());
        var result = joinGroupRequestRepository.save(request);
        var approvers = groupRepository.findAllGroupMembersByPermission(result.getGroup().getId(), GROUP_ADD_MEMBER);
        approvers.forEach(approver ->
                notificationService.createNotification(creator.getId(), result.getGroup().getId(),
                        result.getMessage(), JOIN_GROUP_REQUEST, approver.getId()));
        return result;
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

    @Override
    public void deleteJoinRequestsForUserAndGroup(Long userId, Long groupId) {
        joinGroupRequestRepository.deleteAllByUserIdAndGroupId(userId, groupId);
    }

    @Override
    public boolean groupExistsByIdTag(String idTag, Long groupId) {
        return groupRepository.existsByIdTagAndParentGroupId(idTag, groupId);
    }

    private Group getGroupCopy(Group group) {
        var result = new Group();
        result.setId(group.getId());
        result.setIdTag(group.getIdTag());
        result.setName(group.getName());
        result.setDescription(group.getDescription());
        result.setCreationDateTime(group.getCreationDateTime());
        result.setAvatarUrl(group.getAvatarUrl());
        result.setAvatarThumbnailUrl(group.getAvatarThumbnailUrl());
        result.setMediumAvatarUrl(group.getMediumAvatarUrl());
        result.setDeleteAvatarUrl(group.getDeleteAvatarUrl());
        result.setCreator(group.getCreator());
        result.setParentGroup(group.getParentGroup());
        result.setUserGrants(group.getUserGrants());
        result.setChildGroups(group.getChildGroups());
        result.setScheduleRecords(group.getScheduleRecords());
        result.setChats(group.getChats());
        return result;
    }
}
