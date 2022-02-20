package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.dto.RoleDTO;

import java.util.Collection;

public interface RoleService {
    Collection<RoleDTO> getRoles(int pageNumber, int pageSize);
    RoleDTO findById(Long id);
    RoleDTO create(RoleDTO role);
    RoleDTO update(RoleDTO role);
    boolean deleteById(Long id);
}
