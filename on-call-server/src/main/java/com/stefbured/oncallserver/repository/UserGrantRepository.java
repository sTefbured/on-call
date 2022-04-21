package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.role.UserGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGrantRepository extends JpaRepository<UserGrant, Long> {
}
