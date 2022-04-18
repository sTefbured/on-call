package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.exception.RedirectException;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.GroupService;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.OnCallModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import static com.stefbured.oncallserver.OnCallDefaultPermissions.*;
import static com.stefbured.oncallserver.utils.OnCallModelMapper.*;

@RestController
@RequestMapping("api/v1/group/")
public class GroupController {
    private final GroupService groupService;
    private final OnCallModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, OnCallModelMapper modelMapper, UserService userService) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("**")
    public ResponseEntity<GroupDTO> getGroupByTagSequence(HttpServletRequest request) {
        var tagSequence = request.getRequestURI().replaceFirst("/api/v1/group/", "").split("/");
        var queriedGroup = groupService.getByGroupSequence(tagSequence);
        return createResponseForGetGroupOperation(queriedGroup);
    }

    @GetMapping
    public ResponseEntity<GroupDTO> getGroupById(@RequestParam long groupId) {
        var queriedGroup = groupService.getById(groupId);
        return createResponseForGetGroupOperation(queriedGroup);
    }

    @GetMapping("members") //TODO: edit later
    public ResponseEntity<Set<UserDTO>> getGroupMembers(@RequestParam long groupId,
                                                        @RequestParam int page,
                                                        @RequestParam int pageSize,
                                                        HttpServletRequest httpServletRequest,
                                                        HttpServletResponse httpServletResponse) {
        if (page < 1) {
            var redirectPath = "members?groupId=" + groupId + "&page=1&size=" + pageSize;
            try {
                var authorizationHeader = httpServletRequest.getHeader("Authorization");
                httpServletResponse.addHeader("Authorization", authorizationHeader);
                httpServletRequest.getRequestDispatcher(redirectPath)
                        .forward(httpServletRequest, httpServletResponse);
                return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).body(null);
            } catch (IOException | ServletException e) {
                throw new RedirectException("Exception during redirect to " + redirectPath, e);
            }
        }
        page--;
        var typeMap = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_LIMITED_DTO);
        var groupMembers = groupService.getGroupMembers(groupId, page, pageSize).stream()
                .map(typeMap::map)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(groupMembers);
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO group, HttpServletRequest request) {
        var parentId = group.getParentGroup() == null ? null : group.getParentGroup().getId();
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var hasGlobalPermission = userService.userHasGlobalAuthority(username, GROUP_CREATE);
        if (!hasGlobalPermission
                && (parentId == null || !userService.userHasAuthorityForGroup(username, parentId, GROUP_CREATE))) {
            return ResponseEntity.notFound().build();
        }

        var groupEntity = new Group();
        modelMapper.mapSkippingNullValues(group, groupEntity);
        var createdGroup = groupService.create(groupEntity);

//        TODO: ADD GRANTS FROM PARENT GROUP TO THE CREATOR WHEN GRANTS SERVICE WILL BE IMPLEMENTED
//        if (!hasGlobalPermission) {
//            var parentGrants = userService.getUserByUsername(username).getGrants();
//            var grants = parentGrants.stream()
//                            .map(grant -> grant.set)
//            grant.setId();
//        }

        var result = modelMapper.getTypeMap(Group.class, GroupDTO.class, GROUP_TO_ADMIN_VIEW_DTO).map(createdGroup);
        var locationUri = URI.create(request.getRequestURI()).resolve(result.getId().toString());
        return ResponseEntity.created(locationUri).body(result);
    }

    @PutMapping
    public ResponseEntity<GroupDTO> editGroup(@RequestBody GroupDTO group) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var hasGlobalAuthority = userService.userHasGlobalAuthority(username, GROUP_EDIT);
        if (!hasGlobalAuthority && !userService.userHasAuthorityForGroup(username, group.getId(), GROUP_EDIT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var groupEntity = groupService.getById(group.getId());
        modelMapper.mapSkippingNullValues(group, groupEntity);
        var updatedGroup = groupService.update(groupEntity);
        var result = modelMapper.getTypeMap(Group.class, GroupDTO.class, GROUP_TO_ADMIN_VIEW_DTO).map(updatedGroup);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var hasGlobalAuthority = userService.userHasGlobalAuthority(username, GROUP_DELETE);
        if (!hasGlobalAuthority && !userService.userHasAuthorityForGroup(username, groupId, GROUP_DELETE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        groupService.delete(groupId);
        return ResponseEntity.ok("Group deleted");
    }

    private ResponseEntity<GroupDTO> createResponseForGetGroupOperation(Group group) {
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        GroupDTO result;
        if (userService.userHasGlobalAuthority(username, GROUP_ADMIN_VIEW)
                || userService.userHasAuthorityForGroup(username, group.getId(), GROUP_ADMIN_VIEW)) {
            result = modelMapper.getTypeMap(Group.class, GroupDTO.class, GROUP_TO_ADMIN_VIEW_DTO).map(group);
        } else if (userService.userHasAuthorityForGroup(username, group.getId(), GROUP_MEMBER_VIEW)) {
            result = modelMapper.getTypeMap(Group.class, GroupDTO.class, GROUP_TO_MEMBER_VIEW_DTO).map(group);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(result);
    }
}
