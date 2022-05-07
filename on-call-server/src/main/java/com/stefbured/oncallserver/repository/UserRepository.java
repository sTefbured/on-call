package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.user.User;
import org.springframework.data.domain.Page;
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

    @Query(value = "select u.id, u.username, u.email, u.first_name, u.last_name, u.birth_date, u.registration_date_time," +
            "   u.last_visit_date_time, u.password_expiration_date, u.is_banned, u.is_enabled " +
            "from users u " +
            "join user_grants ug on ug.user_id = u.id " +
            "join groups g on g.id = ug.group_id " +
            "where g.id = ?1", nativeQuery = true)
    Page<User> getGroupMembers(Long groupId, Pageable page);

    List<User> findAllByOrderByUsername(Pageable page);

    @Query(value = "select count(*) " +
            "from users u, user_grants ug, groups g " +
            "where ug.user_id = u.id and g.id = ug.group_id and g.id = ?1", nativeQuery = true)
    Long getGroupMembersCount(Long groupId);

    @Query(value = "select id from users where username = ?1", nativeQuery = true)
    Long getUserIdByUsername(String username);
}
