package com.stefbured.oncallserver.model.entity.user.rights;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "permissions")
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 30)
    private String name;

    @Column(name = "description", length = 50)
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }
}
