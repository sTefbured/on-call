package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.group.UserGroup;
import com.stefbured.oncallserver.model.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findAllByUserGroupsContains(UserGroup group, Pageable page);

    @Query(value = "select id from users where username = ?1", nativeQuery = true)
    Long getUserIdByUsername(String username);
}
