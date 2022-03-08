package com.stefbured.oncallserver.model.entity.user.rights;

import com.stefbured.oncallserver.model.entity.group.UserGroup;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serial;

import static com.stefbured.oncallserver.model.ModelConstants.Permission.MAX_PERMISSION_DESCRIPTION_LENGTH;
import static com.stefbured.oncallserver.model.ModelConstants.Permission.MAX_PERMISSION_NAME_LENGTH;

@Entity
@Data
@NoArgsConstructor
@Table(name = "permissions")
public class Permission implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = -4680858741953145110L;

    @Id
    private Long id;

    @Column(name = "name", length = MAX_PERMISSION_NAME_LENGTH, nullable = false)
    private String name;

    @Column(name = "description", length = MAX_PERMISSION_DESCRIPTION_LENGTH)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    @Override
    public String getAuthority() {
        return name;
    }
}
