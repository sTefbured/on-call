package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.model.entity.role.Role;
import com.stefbured.oncallserver.repository.RoleRepository;
import com.stefbured.oncallserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Collection<Role> getRoles(int page, int pageSize) {
        return roleRepository.findAllByOrderByIdAsc(PageRequest.of(page, pageSize)).toList();
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Role create(Role role) {
        return null;
    }

    @Override
    public Role update(Role role) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return roleRepository.removeById(id);
    }
}
