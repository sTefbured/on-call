package com.stefbured.oncallserver.config;

import com.stefbured.oncallserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class OnCallPermissionEvaluator implements PermissionEvaluator {
    public static final String GLOBAL_TARGET_TYPE = "global";
    public static final String GROUP_TARGET_TYPE = "group";
    public static final String CHAT_TARGET_TYPE = "chat";

    private static OnCallPermissionEvaluator instance;

    private final UserService userService;

    @Autowired
    public OnCallPermissionEvaluator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String contextType, Object permission) {
        var userId = (Long) authentication.getDetails();
        var authority = (String) permission;
        var targetIdLong = (Long) targetId;
        return switch (contextType) {
            case GLOBAL_TARGET_TYPE -> userService.userHasGlobalAuthority(userId, authority);
            case GROUP_TARGET_TYPE -> targetId != null && userService.userHasAuthorityForGroup(userId, targetIdLong, authority);
            case CHAT_TARGET_TYPE -> targetId != null && userService.userHasAuthorityForChat(userId, targetIdLong, authority);
            default -> false;
        };
    }

    public static boolean hasPermission(Long targetId, String contextType, String permission) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (instance == null) {
            instance = SpringContextHolder.getContext().getBean(OnCallPermissionEvaluator.class);
        }
        return instance.hasPermission(authentication, targetId, contextType, permission);
    }

    public static boolean hasPermission(String contextType, String permission) {
        return hasPermission((Long) null, contextType, permission);
    }
}
