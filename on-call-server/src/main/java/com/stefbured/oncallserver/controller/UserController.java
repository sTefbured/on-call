package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.exception.user.UserException;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.model.dto.user.BatchUserRegistrationRecordDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.OnCallModelMapper;
import com.stefbured.oncallserver.utils.PasswordGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.stefbured.oncallserver.utils.OnCallModelMapper.USER_TO_DTO;
import static com.stefbured.oncallserver.utils.OnCallModelMapper.USER_TO_POST_REGISTRATION_DTO;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    private static final int GENERATED_PASSWORD_LENGTH = 16;

    private final UserService userService;
    private final OnCallModelMapper modelMapper;
    private final PasswordGenerator passwordGenerator;

    @Autowired
    public UserController(UserService userService, OnCallModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        passwordGenerator = new PasswordGenerator();
    }

    @GetMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User result;
        try {
            result = userService.getUserById(id);
        } catch (UserNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        var resultDTO = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_DTO).map(result);
        return ResponseEntity.ok(resultDTO);
    }

    @PostMapping
    @PreAuthorize("isAnonymous() || hasAuthority('')")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDTO user) {
        LOGGER.info("Performing single user registration: username={}", user.getUsername());
        try {
            var userEntity = new User();
            modelMapper.mapSkippingNullValues(user, userEntity);
            var typeMap = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_POST_REGISTRATION_DTO);
            var userDetails = typeMap.map(userService.register(userEntity));
            LOGGER.info("Successful registration: user={}", userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDetails);
        } catch (Exception exception) {
            LOGGER.info("Registration failed: {}", exception.getMessage());
            if (exception instanceof UserException) {
                return ResponseEntity.badRequest().body(exception.getMessage());
            } else {
                return ResponseEntity.badRequest().body("Unknown error during registration");
            }
        }
    }

    @PostMapping("batch")
    @PreAuthorize("hasAuthority('register:batch')")
    public ResponseEntity<List<BatchUserRegistrationRecordDTO>> registerMultipleUsers(@RequestBody Set<@Valid UserDTO> users,
                                                                                      @RequestParam(required = false) Boolean generatePassword) {
        LOGGER.info("Batch registration started");
        if (Boolean.TRUE.equals(generatePassword)) {
            users.forEach(user -> user.setPassword(passwordGenerator.generate(GENERATED_PASSWORD_LENGTH)));
        }
        var registrationRecords = new ArrayList<BatchUserRegistrationRecordDTO>(users.size());
        for (var user : users) {
            registrationRecords.add(registerWithErrorHandling(user));
        }
        LOGGER.info("Batch registration finished: {}", registrationRecords);
        return ResponseEntity.ok(registrationRecords);
    }

    @PutMapping
    //TODO: finish
    public ResponseEntity<UserDTO> editUser(@RequestBody @Valid UserDTO user) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userService.userHasAnyAuthority(authentication.getName())) {
            var userEntity = new User();
            modelMapper.mapSkippingNullValues(user, userEntity);
            var typeMap = modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_DTO);
            var result = typeMap.map(userService.updateUser(userEntity));
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping(value = "{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    private BatchUserRegistrationRecordDTO registerWithErrorHandling(UserDTO user) {
        var registrationRecord = new BatchUserRegistrationRecordDTO();
        registrationRecord.setUser(user);
        try {
            var userEntity = new User();
            modelMapper.mapSkippingNullValues(user, userEntity);
            userService.register(userEntity);
            registrationRecord.setSuccessful(true);
        } catch (Exception exception) {
            registrationRecord.setSuccessful(false);
            registrationRecord.setExceptionMessage(exception.getMessage());
        }
        return registrationRecord;
    }
}
