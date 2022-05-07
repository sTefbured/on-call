package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.config.OnCallPermissionEvaluator;
import com.stefbured.oncallserver.model.dto.role.UserGrantDTO;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.service.UserGrantService;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.NoSuchElementException;

import static com.stefbured.oncallserver.OnCallConstants.*;
import static com.stefbured.oncallserver.config.OnCallPermissionEvaluator.*;
import static com.stefbured.oncallserver.mapper.UserGrantModelMapper.USER_GRANT_MODEL_MAPPER;

@RestController
@RequestMapping("api/v1/userGrant")
public class UserGrantController {
    private final UserGrantService userGrantService;
    private final OnCallModelMapper modelMapper;

    @Autowired
    public UserGrantController(UserGrantService userGrantService,
                               @Qualifier(USER_GRANT_MODEL_MAPPER) OnCallModelMapper modelMapper) {
        this.userGrantService = userGrantService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasPermission(#grant.group?.id, '" + GROUP_TARGET_TYPE + "', '" + USER_GRANT_CREATE + "') " +
            "|| hasPermission(#grant.chat?.id, '" + CHAT_TARGET_TYPE + "', '" + USER_GRANT_CREATE + "') " +
            "|| hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + USER_GRANT_CREATE + "')")
    public ResponseEntity<UserGrantDTO> addGrant(@RequestBody UserGrantDTO grant, HttpServletRequest request) {
        var grantEntity = new UserGrant();
        modelMapper.mapSkippingNullValues(grant, grantEntity);
        var createdUserGrant = userGrantService.createUserGrant(grantEntity);
        var result = new UserGrantDTO();
        modelMapper.mapSkippingNullValues(createdUserGrant, result);
        var locationUri = URI.create(request.getRequestURI()).resolve(result.getId().toString());
        return ResponseEntity.created(locationUri).body(result);
    }

    @GetMapping("{userGrantId}")
    @PostAuthorize("hasPermission(returnObject.body.group?.id, '" + GROUP_TARGET_TYPE + "', '" + USER_GRANT_VIEW + "') " +
            "|| hasPermission(returnObject.body.chat?.id, '" + CHAT_TARGET_TYPE + "', '" + USER_GRANT_VIEW + "') " +
            "|| hasPermission(null, '" + GROUP_TARGET_TYPE + "', '" + USER_GRANT_VIEW + "')")
    public ResponseEntity<UserGrantDTO> getUserGrantById(@PathVariable Long userGrantId) {
        UserGrant queriedGrant;
        try {
            queriedGrant = userGrantService.getUserGrantById(userGrantId);
        } catch (NoSuchElementException exception) {
            return ResponseEntity.notFound().build();
        }
        var result = new UserGrantDTO();
        modelMapper.mapSkippingNullValues(queriedGrant, result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{userGrantId}")
    public ResponseEntity<String> deleteUserGrantById(@PathVariable Long userGrantId) {
        var queriedGrant = userGrantService.getUserGrantById(userGrantId);
        var groupId = queriedGrant.getGroup() == null ? null : queriedGrant.getGroup().getId();
        var chatId = queriedGrant.getChat() == null ? null : queriedGrant.getChat().getId();
        if (!OnCallPermissionEvaluator.hasPermission(groupId, GROUP_TARGET_TYPE, USER_GRANT_DELETE)
                && !OnCallPermissionEvaluator.hasPermission(chatId, CHAT_TARGET_TYPE, USER_GRANT_DELETE)
                && !OnCallPermissionEvaluator.hasPermission(GLOBAL_TARGET_TYPE, USER_GRANT_DELETE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userGrantService.deleteUserGrantById(userGrantId);
        return ResponseEntity.ok("Grant deleted");
    }
}
