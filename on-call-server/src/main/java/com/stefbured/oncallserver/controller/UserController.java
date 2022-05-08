package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.config.OnCallPermissionEvaluator;
import com.stefbured.oncallserver.exception.user.UserException;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.ImageUploadingService;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

import static com.stefbured.oncallserver.OnCallConstants.*;
import static com.stefbured.oncallserver.config.OnCallPermissionEvaluator.GLOBAL_TARGET_TYPE;
import static com.stefbured.oncallserver.jwt.JwtConstants.AUTH_COOKIE_NAME;
import static com.stefbured.oncallserver.mapper.UserModelMapper.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final ImageUploadingService imageUploadingService;
    private final OnCallModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService,
                          ImageUploadingService imageUploadingService,
                          @Qualifier(USER_MODEL_MAPPER) OnCallModelMapper modelMapper) {
        this.userService = userService;
        this.imageUploadingService = imageUploadingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{id}")
    @PreAuthorize("authentication.details.equals(#id)" +
            "|| hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + USER_PUBLIC_INFO_VIEW + "')" +
            "|| hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + USER_PRIVATE_INFO_VIEW + "')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User queriedUser;
        try {
            queriedUser = userService.getUserById(id);
        } catch (UserNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        UserDTO result;
        var authenticatedUserId = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (queriedUser.getId().equals(authenticatedUserId)
                || OnCallPermissionEvaluator.hasPermission(GLOBAL_TARGET_TYPE, USER_PRIVATE_INFO_VIEW)) {
            result = modelMapper.map(queriedUser, UserDTO.class, USER_TO_PRIVATE_INFORMATION_DTO);
        } else {
            result = modelMapper.map(queriedUser, UserDTO.class, USER_TO_PUBLIC_INFORMATION_DTO);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("all")
    @PreAuthorize("hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + USER_PUBLIC_INFO_VIEW + "') " +
            "|| hasPermission(null , '" + GLOBAL_TARGET_TYPE + "', '" + USER_PRIVATE_INFO_VIEW + "')")
    public ResponseEntity<Collection<UserDTO>> getUsersList(@RequestParam int page, @RequestParam int pageSize) {
        var users = userService.getUsers(page, pageSize);
        var result = modelMapper.mapCollection(users, UserDTO.class, USER_TO_PREVIEW_DTO);
        var totalUsersCount = userService.getUsersCount();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, String.valueOf(totalUsersCount))
                .body(result);
    }

    @PostMapping
    @PreAuthorize("isAnonymous() || hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + USER_REGISTER + "')")
    public ResponseEntity<Object> registerUser(@RequestPart(required = false) MultipartFile avatar,
                                               @RequestPart @Valid UserDTO user,
                                               HttpServletRequest request) {
        LOGGER.info("Performing single user registration: username={}", user.getUsername());
        try {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                user.setGrants(null);
            }
            if (avatar != null) {
                var avatarUrls = imageUploadingService.loadImage(avatar);
                user.setAvatarUrl(avatarUrls.getImageUrl());
                user.setAvatarThumbnailUrl(avatarUrls.getImageThumbnailUrl());
                user.setMediumAvatarUrl(avatarUrls.getMediumImageUrl());
                user.setDeleteAvatarUrl(avatarUrls.getDeleteImageUrl());
            }
            var userEntity = new User();
            modelMapper.mapSkippingNullValues(user, userEntity);
            var registeredUser = userService.register(userEntity);
            var userDetails = modelMapper.map(registeredUser, UserDTO.class, USER_TO_POST_REGISTRATION_DTO);
            LOGGER.info("Successful registration: user={}", userDetails);
            var locationUri = URI.create(request.getRequestURI()).resolve(userDetails.getId().toString());
            return ResponseEntity.created(locationUri).body(userDetails);
        } catch (UserException exception) {
            LOGGER.info("Registration failed: {}", exception.getMessage());
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping
    @PreAuthorize("authentication.details.equals(#user.id) " +
            "|| hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + USER_EDIT + "')")
    public ResponseEntity<Object> editUser(@RequestBody @Valid UserDTO user) {
        if (user.getId() == null) {
            return ResponseEntity.badRequest().body("UserId wasn't provided");
        }
        var userEntity = new User();
        var authenticatedUserId = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (!user.getId().equals(authenticatedUserId)) {
            user.setPassword(null);
        }
        modelMapper.mapSkippingNullValues(user, userEntity);
        var updatedUser = userService.updateUser(userEntity);
        var result = modelMapper.map(updatedUser, UserDTO.class, USER_TO_PRIVATE_INFORMATION_DTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("me")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        var cookie = new Cookie(AUTH_COOKIE_NAME, "");
        cookie.setSecure(true);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
