package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.user.rights.Role;
import com.stefbured.oncallserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " was not found."));
        var roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        var authorities = user.getAuthorityNames();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles.toArray(String[]::new))
                .authorities(authorities.toArray(String[]::new))
                .accountExpired(isBeforeCurrentDate(user.getUserExpirationDate()))
                .accountLocked(user.isBanned() != null && user.isBanned())
                .credentialsExpired(isBeforeCurrentDate(user.getPasswordExpirationDate()))
                .disabled(!user.isEnabled())
                .build();
    }

    private boolean isBeforeCurrentDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(dateTime);
    }
}
