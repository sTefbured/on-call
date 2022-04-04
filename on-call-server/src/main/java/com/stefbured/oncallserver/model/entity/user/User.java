package com.stefbured.oncallserver.model.entity.user;

import com.stefbured.oncallserver.model.entity.group.UserGroup;
import com.stefbured.oncallserver.model.entity.user.rights.Permission;
import com.stefbured.oncallserver.model.entity.user.rights.Role;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.stefbured.oncallserver.model.ModelConstants.User.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -2624571695743685604L;

    @Id
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = MAX_USERNAME_LENGTH)
    private String username;

    @Column(name = "password", nullable = false, length = MAX_PASSWORD_LENGTH)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = MAX_EMAIL_LENGTH)
    private String email;

    @Column(name = "first_name", length = MAX_NAME_LENGTH)
    private String firstName;

    @Column(name = "last_name", length = MAX_NAME_LENGTH)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "last_visit")
    private LocalDateTime lastVisitDate;

    @Column(name = "password_expiration_date")
    private LocalDateTime passwordExpirationDate;

    @Column(name = "user_expiration_date")
    private LocalDateTime userExpirationDate;

    @Column(name = "is_banned")
    @Type(type = "boolean")
    @Getter(AccessLevel.NONE)
    private Boolean isBanned;

    @Column(name = "is_enabled", nullable = false)
    @Type(type = "boolean")
    @Getter(AccessLevel.NONE)
    private Boolean isEnabled;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "users")
    private Set<Role> roles;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "members")
    private Set<UserGroup> userGroups;

    public Boolean isBanned() {
        return isBanned;
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public Set<String> getAuthorityNames() {
        return getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getAuthority)
                .collect(Collectors.toSet());
    }
}
