package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.group.JoinGroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinGroupRequestRepository extends JpaRepository<JoinGroupRequest, Long> {
}
