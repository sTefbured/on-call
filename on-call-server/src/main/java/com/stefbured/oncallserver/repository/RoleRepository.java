package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.user.rights.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean removeById(Long id);
    Page<Role> findAllByOrderByIdAsc(Pageable pageable);
}
