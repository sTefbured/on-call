package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.group.JoinGroupRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinGroupRequestRepository extends JpaRepository<JoinGroupRequest, Long> {
    Page<JoinGroupRequest> findAllByGroupId(Long groupId, Pageable pageable);
    long countAllByGroupId(Long groupId);
    void deleteAllByUserIdAndGroupId(Long userId, Long groupId);
}
