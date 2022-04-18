package com.stefbured.oncallserver.model.entity.role;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.Role.MAX_ROLE_DESCRIPTION_LENGTH;
import static com.stefbured.oncallserver.model.ModelConstants.Role.MAX_ROLE_NAME_LENGTH;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    private Long id;

    @NonNull
    @Column(name = "name", length = MAX_ROLE_NAME_LENGTH, nullable = false)
    private String name;

    @Column(name = "description", length = MAX_ROLE_DESCRIPTION_LENGTH)
    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_type_id")
    private RoleType roleType;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "role")
    private Set<UserGrant> userGrants;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;
}
