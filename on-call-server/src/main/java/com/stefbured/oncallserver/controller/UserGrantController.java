package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.dto.role.UserGrantDTO;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.service.UserGrantService;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.OnCallModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.NoSuchElementException;

import static com.stefbured.oncallserver.OnCallConstants.*;

@RestController
@RequestMapping("api/v1/userGrant")
public class UserGrantController {
    private final UserService userService;
    private final UserGrantService userGrantService;
    private final OnCallModelMapper modelMapper;

    @Autowired
    public UserGrantController(UserService userService,
                               UserGrantService userGrantService,
                               OnCallModelMapper modelMapper) {
        this.userService = userService;
        this.userGrantService = userGrantService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<UserGrantDTO> addGrant(@RequestBody UserGrantDTO grant, HttpServletRequest request) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var grantEntity = new UserGrant();
        modelMapper.mapSkippingNullValues(grant, grantEntity);
        var hasGlobalPermission = userService.userHasGlobalAuthority(username, USER_GRANT_CREATE);
        if (!hasGlobalPermission
                && (grantEntity.getGroup() == null
                    || !userService.userHasAuthorityForGroup(username, grantEntity.getGroup().getId(), USER_GRANT_CREATE))
                && (grantEntity.getChat() == null)
                    || !userService.userHasAuthorityForChat(username, grantEntity.getChat().getId(), USER_GRANT_CREATE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var createdUserGrant = userGrantService.createUserGrant(grantEntity);
        var result = new UserGrantDTO();
        modelMapper.mapSkippingNullValues(createdUserGrant, result);
        var locationUri = URI.create(request.getRequestURI()).resolve(result.getId().toString());
        return ResponseEntity.created(locationUri).body(result);
    }

    @GetMapping("{userGrantId}")
    public ResponseEntity<UserGrantDTO> getUserGrantById(@PathVariable Long userGrantId) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var hasGlobalPermission = userService.userHasGlobalAuthority(username, USER_GRANT_VIEW);
        UserGrant queriedGrant;
        try {
            queriedGrant = userGrantService.getUserGrantById(userGrantId);
        } catch (NoSuchElementException exception) {
            return ResponseEntity.notFound().build();
        }
        if (!hasGlobalPermission
                && (queriedGrant.getGroup() == null
                    || !userService.userHasAuthorityForGroup(username, queriedGrant.getGroup().getId(), USER_GRANT_VIEW))
                && (queriedGrant.getChat() == null
                    || !userService.userHasAuthorityForChat(username, queriedGrant.getChat().getId(), USER_GRANT_VIEW))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var result = new UserGrantDTO();
        modelMapper.mapSkippingNullValues(queriedGrant, result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{userGrantId}")
    public ResponseEntity<Void> deleteUserGrantById(@PathVariable Long userGrantId) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var hasGlobalPermission = userService.userHasGlobalAuthority(username, USER_GRANT_DELETE);
        var queriedGrant = userGrantService.getUserGrantById(userGrantId);
        if (!hasGlobalPermission
                && (queriedGrant.getGroup() == null
                || !userService.userHasAuthorityForGroup(username, queriedGrant.getGroup().getId(), USER_GRANT_DELETE))
                && (queriedGrant.getChat() == null
                || !userService.userHasAuthorityForChat(username, queriedGrant.getChat().getId(), USER_GRANT_DELETE))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userGrantService.deleteUserGrantById(userGrantId);
        return ResponseEntity.ok().build();
    }
}
