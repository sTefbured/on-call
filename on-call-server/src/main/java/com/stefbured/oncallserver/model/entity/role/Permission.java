package com.stefbured.oncallserver.model.entity.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.Permission.MAX_PERMISSION_DESCRIPTION_LENGTH;
import static com.stefbured.oncallserver.model.ModelConstants.Permission.MAX_PERMISSION_NAME_LENGTH;

@Entity
@Data
@NoArgsConstructor
@Table(name = "permissions")
public class Permission implements GrantedAuthority {
    @Id
    private Long id;

    @Column(name = "name", length = MAX_PERMISSION_NAME_LENGTH, nullable = false)
    private String name;

    @Column(name = "description", length = MAX_PERMISSION_DESCRIPTION_LENGTH)
    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "permissions")
    private transient Set<Role> roles;

    @Override
    public String getAuthority() {
        return name;
    }
}
