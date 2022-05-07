package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.user.UserAlreadyExistsException;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.model.entity.role.Role;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.UserGrantService;
import com.stefbured.oncallserver.service.UserService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.stefbured.oncallserver.OnCallConstants.ON_CALL_USER_ROLE_ID;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_MODEL_MAPPER;

@Service
public class UserServiceImpl implements UserService {
    private static final Set<Long> DEFAULT_USER_ROLES_IDS = new HashSet<>();

    static {
        DEFAULT_USER_ROLES_IDS.add(ON_CALL_USER_ROLE_ID);
    }

    private final LongPrimaryKeyGenerator primaryKeyGenerator;
    private final UserRepository userRepository;
    private final UserGrantService userGrantService;
    private final OnCallModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(LongPrimaryKeyGenerator primaryKeyGenerator,
                           UserRepository userRepository,
                           UserGrantService userGrantService,
                           @Qualifier(USER_MODEL_MAPPER) OnCallModelMapper modelMapper,
                           PasswordEncoder passwordEncoder) {
        this.primaryKeyGenerator = primaryKeyGenerator;
        this.userRepository = userRepository;
        this.userGrantService = userGrantService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        checkUserUniqueValues(user);
        var grants = user.getGrants() == null ? new ArrayList<UserGrant>() : user.getGrants();
        user.setGrants(null);
        var userId = primaryKeyGenerator.generatePk(User.class);
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsBanned(false);
        user.setIsEnabled(true);
        user.setRegistrationDateTime(LocalDateTime.now());
        user.setLastVisitDateTime(LocalDateTime.now());
        var createdUser = userRepository.save(user);
        DEFAULT_USER_ROLES_IDS.forEach(roleId -> {
            if (grants.stream().noneMatch(grant -> grant.getRole().getId().equals(roleId)
                    && grant.getGroup() == null && grant.getChat() == null)) {
                var grant = new UserGrant();
                var role = new Role();
                role.setId(roleId);
                grant.setRole(role);
                grants.add(grant);
            }
        });
        grants.forEach(grant -> {
            grant.setUser(createdUser);
            userGrantService.createUserGrant(grant);
        });
        return getUserById(userId);
    }

    private void checkUserUniqueValues(User user) {
        var result = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        result.ifPresent(u -> {
            String exceptionMessage;
            if (u.getId().equals(user.getId())) {
                return;
            }
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
    public Collection<User> getUsers(int page, int pageSize) {
        return userRepository.findAllByOrderByUsername(Pageable.ofSize(pageSize).withPage(page));
    }

    @Override
    public long getUsersCount() {
        return userRepository.count();
    }

    @Override
    public User updateUser(User user) {
        checkUserUniqueValues(user);
        var queriedUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        modelMapper.mapSkippingNullValues(user, queriedUser);
        return userRepository.save(queriedUser);
    }

    @Override
    public boolean userHasGlobalAuthority(Long userId, String authority) {
        var user = getUserById(userId);
        return user.getGrants().stream()
                .filter(UserGrant::isGlobal)
                .flatMap(grant -> grant.getRole().getPermissions().stream())
                .anyMatch(permission -> authority.equals(permission.getAuthority()));
    }

    @Override
    public boolean userHasAuthorityForGroup(Long userId, Long groupId, String authority) {
        var user = getUserById(userId);
        return user.getGrants().stream()
                .filter(grant -> {
                    var group = grant.getGroup();
                    return group != null && groupId.equals(group.getId());
                })
                .flatMap(grant -> grant.getRole().getPermissions().stream())
                .anyMatch(permission -> authority.equals(permission.getAuthority()));
    }

    @Override
    public boolean userHasAuthorityForChat(Long userId, Long chatId, String authority) {
        var user = getUserById(userId);
        return user.getGrants().stream()
                .filter(grant -> {
                    var chat = grant.getChat();
                    return chat != null && chatId.equals(chat.getId());
                })
                .flatMap(grant -> grant.getRole().getPermissions().stream())
                .anyMatch(permission -> authority.equals(permission.getAuthority()));
    }

    @Override
    @Transactional
    public Collection<String> getAuthorityNamesForUserId(Long id) {
        return getUserById(id).getAuthorityNames();
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.getUserIdByUsername(username);
    }
}
