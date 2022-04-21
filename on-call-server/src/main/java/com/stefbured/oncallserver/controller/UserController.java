package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.exception.user.UserException;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.OnCallModelMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import static com.stefbured.oncallserver.OnCallConstants.*;
import static com.stefbured.oncallserver.utils.OnCallModelMapper.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final OnCallModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, OnCallModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('" + USER_PUBLIC_INFO_VIEW + "', '" + USER_PRIVATE_INFO_VIEW + "')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User queriedUser;
        try {
            queriedUser = userService.getUserById(id);
        } catch (UserNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        UserDTO result;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var isReceiversProfile = queriedUser.getUsername().equals(authentication.getName());
        var authorities = authentication.getAuthorities();
        if (isReceiversProfile
                || authorities.stream().anyMatch(a -> a.getAuthority().equals(USER_PRIVATE_INFO_VIEW))) {
            result = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_PRIVATE_INFORMATION_DTO).map(queriedUser);
        } else {
            result = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_PUBLIC_INFORMATION_DTO).map(queriedUser);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("all")
    @PreAuthorize("hasAnyAuthority('" + USER_PUBLIC_INFO_VIEW + "', '" + USER_PRIVATE_INFO_VIEW + "')")
    public ResponseEntity<Set<UserDTO>> getUsersList(@RequestParam int page, @RequestParam int pageSize) {
        var users = userService.getUsers(page, pageSize);
        var typeMap = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_PREVIEW_DTO);
        var result = users.stream().map(typeMap::map).collect(Collectors.toSet());
        var totalUsersCount = userService.getUsersCount();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, String.valueOf(totalUsersCount))
                .body(result);
    }

    @PostMapping
    @PreAuthorize("isAnonymous() || hasAuthority('" + USER_REGISTER + "')")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDTO user, HttpServletRequest request) {
        LOGGER.info("Performing single user registration: username={}", user.getUsername());
        try {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                user.setGrants(null);
            }
            var userEntity = new User();
            modelMapper.mapSkippingNullValues(user, userEntity);
            var typeMap = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_POST_REGISTRATION_DTO);
            var userDetails = typeMap.map(userService.register(userEntity));
            LOGGER.info("Successful registration: user={}", userDetails);
            var locationUri = URI.create(request.getRequestURI()).resolve(userDetails.getId().toString());
            return ResponseEntity.created(locationUri).body(userDetails);
        } catch (UserException exception) {
            LOGGER.info("Registration failed: {}", exception.getMessage());
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping
    @PreAuthorize("isAuthenticated() && (hasAuthority('" + USER_EDIT + "') || #user.getUsername().equals(authentication.name))")
    public ResponseEntity<Object> editUser(@RequestBody @Valid UserDTO user) {
        if (user.getId() == null) {
            return ResponseEntity.badRequest().body("UserId wasn't provided");
        }
        var userEntity = new User();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!user.getUsername().equals(authentication.getName())) {
            user.setPassword(null);
        }
        modelMapper.mapSkippingNullValues(user, userEntity);
        var typeMap = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_PRIVATE_INFORMATION_DTO);
        var result = typeMap.map(userService.updateUser(userEntity));
        return ResponseEntity.ok(result);
    }
}
