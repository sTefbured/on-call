package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByParentGroupAndIdTag(Group parentGroup, String idTag);

    @Query(value = "select * from groups where parent_group_id is NULL", nativeQuery = true)
    Page<Group> findAllFirstLevel(Pageable pageable);

    @Query(value = "select count(*) from groups where parent_group_id is NULL", nativeQuery = true)
    long findAllFirstLevelCount();

    @Query(value = "" +
            "select u.id, u.avatar_thumbnail_url, u.avatar_url, u.birth_date, " +
            "       u.delete_avatar_url, u.email, u.first_name, u.is_banned, " +
            "       u.is_enabled, u.last_name, u.last_visit_date_time, " +
            "       u.medium_avatar_url, u.password, u.password_expiration_date, " +
            "       u.registration_date_time, u.username " +
            "from users u " +
            "join user_grants ug on u.id = ug.user_id and ug.group_id = :1 " +
            "join roles_permissions rp on ug.role_id = rp.role_id " +
            "join permissions p on rp.permission_id = p.id " +
            "where p.name = :2", nativeQuery = true)
    Collection<User> findAllGroupMembersByPermission(Long groupId, String permission);

//    @Query(value = "" +
//            "select id, creation_date_time, description, id_tag, name, creator_id, parent_group_id from ( " +
//            "    with recursive temp(id, creation_date_time, description, id_tag, name, creator_id, parent_group_id, i) as ( " +
//            "    select z.id, z.creation_date_time, z.description, z.id_tag, z.name, z.creator_id, z.parent_group_id, 1 " +
//            "    from groups z " +
//            "    where z.parent_group_id is null " +
//            "      and z.id_tag = ( " +
//            "        select id_tag " +
//            "        from ( values (:tags)) as n " +
//            "        limit 1 offset 0 " +
//            "    ) " +
//            "    union " +
//            "    select g.id, g.creation_date_time, g.description, g.id_tag, g.name, g.creator_id, g.parent_group_id, t.i + 1 " +
//            "    from groups g, temp t " +
//            "    where g.parent_group_id = t.id and g.id_tag = ( " +
//            "        select m.id_tag " +
//            "        from ( values (:tags)) as m(id_tag) " +
//            "        limit 1 offset t.i " +
//            "    ) " +
//            ") " +
//            "select * from temp order by i desc limit 1) result",nativeQuery = true)
//    Optional<Group> findByIdTagSequence(@Param("tags") List<String> idTagSequence);
}
