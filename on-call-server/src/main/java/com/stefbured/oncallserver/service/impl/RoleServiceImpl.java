package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.model.dto.RoleDTO;
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
    public Collection<RoleDTO> getRoles(int page, int pageSize) {
        return roleRepository.findAllByOrderByIdAsc(PageRequest.of(page, pageSize))
                .map(r -> new RoleDTO(r.getId(), r.getName(), r.getDescription(), null, null))
                .toList();
    }

    @Override
    public RoleDTO findById(Long id) {
        return roleRepository.findById(id)
                .map(r -> new RoleDTO(r.getId(), r.getName(), r.getDescription(), null, null))
                .orElseThrow(() -> new UserNotFoundException("No user with id=" + id));
    }

    @Override
    public RoleDTO create(RoleDTO role) {
        return null;
    }

    @Override
    public RoleDTO update(RoleDTO role) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return roleRepository.removeById(id);
    }
}
