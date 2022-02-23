package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.exception.RedirectException;
import com.stefbured.oncallserver.model.dto.UserGroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("api/v1/group/")
public class UserGroupController {
    private final UserGroupService userGroupService;

    @Autowired
    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @GetMapping("**")
    public UserGroupDTO getUserGroup(HttpServletRequest request) {
        var groupSequence = request.getRequestURI().replaceFirst("/api/v1/group/", "").split("/");
        return userGroupService.getByGroupSequence(groupSequence);
    }

    @GetMapping("members")
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
        var groupMembers = userGroupService.getGroupMembers(groupId, page, pageSize);
        return new ResponseEntity<>(groupMembers, HttpStatus.OK);
    }
}
