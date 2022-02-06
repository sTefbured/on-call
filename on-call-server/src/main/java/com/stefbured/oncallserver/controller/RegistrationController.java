package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.exception.user.UserException;
import com.stefbured.oncallserver.model.dto.user.BatchUserRegistrationRecord;
import com.stefbured.oncallserver.model.dto.user.UserRegisterDTO;
import com.stefbured.oncallserver.service.user.UserService;
import com.stefbured.oncallserver.utils.PasswordGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private static final Logger LOGGER = LogManager.getLogger(RegistrationController.class);
    private static final String USER_PAGE_ENDPOINT = "user";
    private static final int GENERATED_PASSWORD_LENGTH = 16;

    private final UserService userService;
    private final PasswordGenerator passwordGenerator;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
        passwordGenerator = new PasswordGenerator();
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Object> registerSingleUser(@RequestBody UserRegisterDTO user, HttpServletResponse httpServletResponse) {
        var securityContext = SecurityContextHolder.getContext();
        if (!(securityContext.getAuthentication() instanceof AnonymousAuthenticationToken)) {
            redirectToProfilePage(securityContext, httpServletResponse);
            return null;
        }
        LOGGER.info("Performing single user registration: username={}", user.getUsername());
        try {
            var userDetails = userService.register(user);
            ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(userDetails);
            LOGGER.info("Successful registration: user={}", userDetails);
            return responseEntity;
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
    public List<BatchUserRegistrationRecord> registerMultipleUsers(@RequestBody Set<UserRegisterDTO> users,
                                                                   @RequestParam(required = false) Boolean generatePassword) {
        LOGGER.info("Batch registration started");
        if (Boolean.TRUE.equals(generatePassword)) {
            users.forEach(user -> user.setPassword(passwordGenerator.generate(GENERATED_PASSWORD_LENGTH)));
        }
        var registrationRecords = new ArrayList<BatchUserRegistrationRecord>(users.size());
        for (var user : users) {
            registrationRecords.add(registerWithErrorHandling(user));
        }
        LOGGER.info("Batch registration finished: {}", registrationRecords);
        return registrationRecords;
    }

    private BatchUserRegistrationRecord registerWithErrorHandling(UserRegisterDTO user) {
        var registrationRecord = new BatchUserRegistrationRecord();
        registrationRecord.setUser(user);
        try {
            userService.register(user);
            registrationRecord.setSuccessful(true);
        } catch (Exception exception) {
            registrationRecord.setSuccessful(false);
            registrationRecord.setExceptionMessage(exception.getMessage());
        }
        return registrationRecord;
    }

    private void redirectToProfilePage(SecurityContext securityContext, HttpServletResponse httpServletResponse) {
        var username = securityContext.getAuthentication().getName();
        var userEntity = userService.getByUsername(username);
        var redirectLocation = USER_PAGE_ENDPOINT + "/" + userEntity.getId();
        try {
            httpServletResponse.sendRedirect(redirectLocation);
        } catch (IOException e) {
            LOGGER.error("Error while sending redirect: location={}", redirectLocation);
            throw new IllegalStateException(e);
        }
    }
}