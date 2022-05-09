package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.config.OnCallPermissionEvaluator;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.group.JoinGroupRequestDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.group.JoinGroupRequest;
import com.stefbured.oncallserver.model.entity.role.Role;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.GroupService;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.service.UserGrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collection;

import static com.stefbured.oncallserver.OnCallConstants.*;
import static com.stefbured.oncallserver.config.OnCallPermissionEvaluator.GLOBAL_TARGET_TYPE;
import static com.stefbured.oncallserver.config.OnCallPermissionEvaluator.GROUP_TARGET_TYPE;
import static com.stefbured.oncallserver.mapper.GroupModelMapper.*;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_MODEL_MAPPER;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PREVIEW_DTO;

@RestController
@RequestMapping("api/v1/group")
public class GroupController {
    private final GroupService groupService;
    private final UserGrantService userGrantService;
    private final OnCallModelMapper groupMapper;
    private final OnCallModelMapper userMapper;

    @Autowired
    public GroupController(GroupService groupService,
                           UserGrantService userGrantService,
                           @Qualifier(GROUP_MODEL_MAPPER) OnCallModelMapper groupMapper,
                           @Qualifier(USER_MODEL_MAPPER) OnCallModelMapper userMapper) {
        this.groupService = groupService;
        this.userGrantService = userGrantService;
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("all")
    @PreAuthorize("hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + GROUP_PUBLIC_INFO_VIEW + "')")
    public ResponseEntity<Iterable<GroupDTO>> getFirstLevelGroupsList(@RequestParam int page, @RequestParam int pageSize) {
        var groups = groupService.getFirstLevelGroups(page, pageSize);
        var result = groupMapper.mapCollection(groups, GroupDTO.class, GROUP_TO_PREVIEW_DTO);
        var totalGroupsCount = groupService.getFirstLevelGroupsCount();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, String.valueOf(totalGroupsCount))
                .body(result);
    }

    // Security validation is performed in createResponseForGetGroupOperation() method
    @GetMapping("seq/**")
    public ResponseEntity<GroupDTO> getGroupByTagSequence(HttpServletRequest request) {
        var tagSequence = request.getRequestURI().replaceFirst("/api/v1/group/seq/", "").split("/");
        var queriedGroup = groupService.getByGroupSequence(tagSequence);
        return createResponseForGetGroupOperation(queriedGroup);
    }

    // Security validation is performed in createResponseForGetGroupOperation() method
    @GetMapping("{groupId}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable long groupId) {
        var queriedGroup = groupService.getById(groupId);
        return createResponseForGetGroupOperation(queriedGroup);
    }

    @GetMapping("members")
    @PreAuthorize("hasPermission(null, '" + GROUP_TARGET_TYPE + "', '" + GROUP_MEMBER_VIEW + "') " +
            "&& hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + USER_PUBLIC_INFO_VIEW + "')")
    public ResponseEntity<Collection<UserDTO>> getGroupMembers(@RequestParam long groupId,
                                                               @RequestParam int page,
                                                               @RequestParam int pageSize) {
        var groupMembers = groupService.getGroupMembers(groupId, page, pageSize);
        var totalGroupMembersCount = groupService.getGroupMembersCount(groupId);
        var result = userMapper.mapCollection(groupMembers, UserDTO.class, USER_TO_PREVIEW_DTO);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, String.valueOf(totalGroupMembersCount))
                .body(result);
    }

    // TODO: consider changing like in ChatController
    @PostMapping
    @PreAuthorize("#group.parentGroup == null " +
            "? hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + GROUP_CREATE + "') " +
            ": hasPermission(#group.parentGroup.id, '" + GROUP_TARGET_TYPE + "', '" + GROUP_CREATE + "')")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO group, HttpServletRequest request) {
        var groupEntity = new Group();
        var userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        var user = new User();
        user.setId(userId);
        groupMapper.mapSkippingNullValues(group, groupEntity);
        groupEntity.setCreator(user);
        var createdGroup = groupService.create(groupEntity);
        var userGrant = new UserGrant();
        userGrant.setGroup(createdGroup);
        userGrant.setUser(user);
        var role = new Role();
        role.setId(GROUP_ADMINISTRATOR);
        userGrant.setRole(role);
        userGrantService.createUserGrant(userGrant);
        var grantedGroup = groupService.getById(createdGroup.getId());
        var result = groupMapper.map(grantedGroup, GroupDTO.class, GROUP_TO_ADMIN_VIEW_DTO);
        var locationUri = URI.create(request.getRequestURI()).resolve(result.getIdTag());
        return ResponseEntity.created(locationUri).body(result);
    }

    @PostMapping("join")
    @PreAuthorize("#request.group != null && (#request.group.parentGroup == null " +
            "? hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + GROUP_CREATE_JOIN_REQUEST + "') " +
            ": hasPermission(#request.group.parentGroup.id, '" + GROUP_TARGET_TYPE + "', '" + GROUP_CREATE_JOIN_REQUEST + "'))")
    public ResponseEntity<JoinGroupRequestDTO> createJoinRequest(@RequestBody JoinGroupRequestDTO request) {
        var group = request.getGroup();
        var queriedGroup = groupService.getById(group.getId());
        if (queriedGroup.getParentGroup() != null && group.getParentGroup() == null) {
            return ResponseEntity.badRequest().build();
        }
        var requestEntity = new JoinGroupRequest();
        groupMapper.mapSkippingNullValues(request, requestEntity);
        var createdRequest = groupService.createJoinRequest(requestEntity);
        var result = new JoinGroupRequestDTO();
        groupMapper.mapSkippingNullValues(createdRequest, result);
        return ResponseEntity.accepted().body(result);
    }

    @PutMapping
    @PreAuthorize("hasPermission(#group.id, '" + GROUP_TARGET_TYPE + "', '" + GROUP_EDIT + "') " +
            "|| hasPermission(null , '" + GLOBAL_TARGET_TYPE + "', '" + GROUP_EDIT + "')")
    public ResponseEntity<GroupDTO> editGroup(@RequestBody GroupDTO group) {
        var groupEntity = groupService.getById(group.getId());
        groupMapper.mapSkippingNullValues(group, groupEntity);
        var updatedGroup = groupService.update(groupEntity);
        var result = groupMapper.map(updatedGroup, GroupDTO.class, GROUP_TO_ADMIN_VIEW_DTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{groupId}")
    @PreAuthorize("hasPermission(#groupId, '" + GROUP_TARGET_TYPE + "', '" + GROUP_DELETE + "') " +
            "|| hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + GROUP_DELETE + "')")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId) {
        groupService.delete(groupId);
        return ResponseEntity.ok("Group deleted");
    }

    private ResponseEntity<GroupDTO> createResponseForGetGroupOperation(Group group) {
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        GroupDTO result;
        if (OnCallPermissionEvaluator.hasPermission(group.getId(), GROUP_TARGET_TYPE, GROUP_ADMIN_VIEW)
                || OnCallPermissionEvaluator.hasPermission(GLOBAL_TARGET_TYPE, GROUP_ADMIN_VIEW)) {
            result = groupMapper.map(group, GroupDTO.class, GROUP_TO_ADMIN_VIEW_DTO);
            result.setIsMember(true);
        } else if (OnCallPermissionEvaluator.hasPermission(group.getId(), GROUP_TARGET_TYPE, GROUP_MEMBER_VIEW)
                || OnCallPermissionEvaluator.hasPermission(GLOBAL_TARGET_TYPE, GROUP_MEMBER_VIEW)) {
            result = groupMapper.map(group, GroupDTO.class, GROUP_TO_MEMBER_VIEW_DTO);
            result.setIsMember(true);
        } else if (OnCallPermissionEvaluator.hasPermission(GLOBAL_TARGET_TYPE, GROUP_PUBLIC_INFO_VIEW)) {
            result = groupMapper.map(group, GroupDTO.class, GROUP_TO_PREVIEW_DTO);
            result.setIsMember(false);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(result);
    }
}
