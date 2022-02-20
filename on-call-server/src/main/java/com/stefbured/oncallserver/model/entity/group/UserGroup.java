package com.stefbured.oncallserver.model.entity.group;

import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.user.rights.Permission;
import com.stefbured.oncallserver.model.entity.user.rights.Role;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@Table(name = "user_groups")
public class UserGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = 703295853496190010L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private transient User creator;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private transient User owner;

    @ManyToOne
    @JoinColumn(name = "parent_group_id")
    private transient UserGroup parentGroup;

    @ManyToMany
    @JoinTable(name = "user_groups_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private transient Set<User> members;

    @OneToMany(mappedBy = "userGroup")
    private Set<Permission> permissions;

    @OneToMany(mappedBy = "userGroup")
    private Set<Role> roles;
}
