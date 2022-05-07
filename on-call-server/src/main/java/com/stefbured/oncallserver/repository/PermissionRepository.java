package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.role.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
