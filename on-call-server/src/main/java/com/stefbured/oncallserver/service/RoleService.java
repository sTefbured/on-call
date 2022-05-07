package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.role.Role;

import java.util.Collection;

public interface RoleService {
    Collection<Role> getRoles(int pageNumber, int pageSize);
    Role findById(Long id);
    Role create(Role role);
    Role update(Role role);
    boolean deleteById(Long id);
}
