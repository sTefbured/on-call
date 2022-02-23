package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.group.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    UserGroup getByParentGroupAndIdTag(UserGroup parentGroup, String idTag);
}
