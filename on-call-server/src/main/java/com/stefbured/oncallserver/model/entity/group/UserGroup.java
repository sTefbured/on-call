package com.stefbured.oncallserver.model.entity.group;

import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.user.rights.Permission;
import com.stefbured.oncallserver.model.entity.user.rights.Role;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.UserGroup.*;

@Entity
@Data
@Table(name = "user_groups")
public class UserGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = 703295853496190010L;

    @Id
    private Long id;

    @Column(name = "id_tag", length = MAX_USER_GROUP_ID_TAG_LENGTH)
    private String idTag;

    @Column(name = "name", length = MAX_USER_GROUP_NAME_LENGTH, nullable = false)
    private String name;

    @Column(name = "description", length = MAX_USER_GROUP_DESCRIPTION_LENGTH)
    private String description;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_group_id")
    private UserGroup parentGroup;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_groups_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<User> members;

    @OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY)
    private Set<Permission> permissions;

    @OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY)
    private Set<Role> roles;
}
