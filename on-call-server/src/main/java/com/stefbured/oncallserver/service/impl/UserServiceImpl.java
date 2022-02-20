package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.user.InvalidUserParametersException;
import com.stefbured.oncallserver.exception.user.UserAlreadyExistsException;
import com.stefbured.oncallserver.model.dto.user.UserRegisterDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.user.rights.Role;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.OnCallEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final Set<Role> DEFAULT_USER_ROLES = new HashSet<>() {
        static {
            new Role(1L, "user");
        }
    };

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(UserRegisterDTO user) {
        return registerWithRoles(user, DEFAULT_USER_ROLES);
    }

    @Override
    public User registerWithRoles(UserRegisterDTO user, Set<Role> roles) {
        if (!OnCallEntityValidator.isValid(user)) {
            throw new InvalidUserParametersException("Invalid user parameters");
        }
        checkUserUniqueValues(user);
        roles.addAll(DEFAULT_USER_ROLES);
        var userEntity = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .registrationDate(LocalDateTime.now())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isBanned(false)
                .isEnabled(true)
                .lastVisitDate(LocalDateTime.now())
                .roles(roles)
                .build();
        return userRepository.save(userEntity);
    }

    private void checkUserUniqueValues(UserRegisterDTO userRegisterDTO) {
        var dbUser = userRepository.findByUsernameOrEmail(userRegisterDTO.getUsername(), userRegisterDTO.getEmail());
        dbUser.ifPresent(user -> {
            String exceptionMessage;
            if (user.getUsername().equals(userRegisterDTO.getUsername())) {
                exceptionMessage = "User with such username already exists";
            } else {
                exceptionMessage = "User with such email already exists";
            }
            throw new UserAlreadyExistsException(exceptionMessage);
        });
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElse(new User());
    }
}
