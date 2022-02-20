package com.stefbured.oncallserver.model.entity.user.rights;

import com.stefbured.oncallserver.model.entity.group.UserGroup;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serial;

@Entity
@Data
@NoArgsConstructor
@Table(name = "permissions")
public class Permission implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = -4680858741953145110L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "description", length = 50)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_group_id")
    private transient UserGroup userGroup;

    @Override
    public String getAuthority() {
        return name;
    }
}
