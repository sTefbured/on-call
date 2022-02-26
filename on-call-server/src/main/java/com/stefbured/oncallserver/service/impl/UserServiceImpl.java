package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.user.InvalidUserParametersException;
import com.stefbured.oncallserver.exception.user.UserAlreadyExistsException;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.model.dto.RoleDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.OnCallEntityValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.stefbured.oncallserver.config.ModelMapperConfiguration.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Set<Long> DEFAULT_USER_ROLES_IDS = new HashSet<>();

    static {
        DEFAULT_USER_ROLES_IDS.add(1L);
    }

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO register(UserDTO user) {
        return registerWithRoles(user, DEFAULT_USER_ROLES_IDS);
    }

    @Override
    public UserDTO registerWithRoles(UserDTO userDto, Set<Long> rolesIds) {
        if (!OnCallEntityValidator.isValid(userDto)) {
            throw new InvalidUserParametersException("Invalid user parameters");
        }
        checkUserUniqueValues(userDto);
        rolesIds.addAll(DEFAULT_USER_ROLES_IDS);
        var roles = rolesIds.stream()
                .map(id -> {
                    var role = new RoleDTO();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
        userDto.setRoles(roles);
        var user = modelMapper.getTypeMap(UserDTO.class, User.class, REGISTRATION_DTO_TO_USER).map(userDto);
        var registeredUser = userRepository.save(user);
        return modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_POST_REGISTRATION_DTO).map(registeredUser);
    }

    private void checkUserUniqueValues(UserDTO userDTO) {
        var dbUser = userRepository.findByUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail());
        dbUser.ifPresent(user -> {
            String exceptionMessage;
            if (user.getUsername().equals(userDTO.getUsername())) {
                exceptionMessage = "User with such username already exists";
            } else {
                exceptionMessage = "User with such email already exists";
            }
            throw new UserAlreadyExistsException(exceptionMessage);
        });
    }

    @Override
    public UserDTO getByUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return modelMapper.getTypeMap(User.class, UserDTO.class, USER_TO_DTO).map(user);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.getUserIdByUsername(username);
    }
}
