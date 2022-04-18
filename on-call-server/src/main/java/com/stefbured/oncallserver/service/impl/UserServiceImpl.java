package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.user.UserAlreadyExistsException;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.model.dto.role.RoleDTO;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import com.stefbured.oncallserver.utils.OnCallModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Set<Long> DEFAULT_USER_ROLES_IDS = new HashSet<>();

    static {
        DEFAULT_USER_ROLES_IDS.add(1L);
    }

    private final LongPrimaryKeyGenerator primaryKeyGenerator;
    private final UserRepository userRepository;
    private final OnCallModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(LongPrimaryKeyGenerator primaryKeyGenerator,
                           UserRepository userRepository,
                           OnCallModelMapper modelMapper,
                           PasswordEncoder passwordEncoder) {
        this.primaryKeyGenerator = primaryKeyGenerator;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        return registerWithRoles(user, DEFAULT_USER_ROLES_IDS);
    }

    @Override
    public User registerWithRoles(User user, Set<Long> rolesIds) {
        checkUserUniqueValues(user);
        rolesIds.addAll(DEFAULT_USER_ROLES_IDS);
        //TODO: finish
        var roles = rolesIds.stream()
                .map(id -> {
                    var role = new RoleDTO();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
//        userDto.setRoles(roles);
        user.setId(primaryKeyGenerator.generatePk(User.class));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsBanned(false);
        user.setIsEnabled(true);
        user.setRegistrationDateTime(LocalDateTime.now());
        user.setLastVisitDateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    private void checkUserUniqueValues(User user) {
        var result = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        result.ifPresent(u -> {
            String exceptionMessage;
            if (u.getUsername().equals(user.getUsername())) {
                exceptionMessage = "User with such username already exists";
            } else {
                exceptionMessage = "User with such email already exists";
            }
            throw new UserAlreadyExistsException(exceptionMessage);
        });
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateUser(User user) {
        checkUserUniqueValues(user);
        var queriedUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        modelMapper.mapSkippingNullValues(user, queriedUser);
        return userRepository.save(queriedUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean userHasAnyAuthority(String username, String... authorities) {
        var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return Collections.disjoint(user.getAuthorityNames(), Arrays.asList(authorities));
    }

    @Override
    public boolean userHasGlobalAuthority(String username, String authority) {
        var user = getUserByUsername(username);
        return user.getGrants().stream()
                .filter(UserGrant::isGlobal)
                .flatMap(grant -> grant.getRole().getPermissions().stream())
                .anyMatch(permission -> authority.equals(permission.getAuthority()));
    }

    @Override
    public boolean userHasAuthorityForGroup(String username, Long groupId, String authority) {
        var user = getUserByUsername(username);
        return user.getGrants().stream()
                .filter(grant -> {
                    var group = grant.getGroup();
                    return group != null && groupId.equals(group.getId());
                })
                .flatMap(grant -> grant.getRole().getPermissions().stream())
                .anyMatch(permission -> authority.equals(permission.getAuthority()));
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.getUserIdByUsername(username);
    }
}
