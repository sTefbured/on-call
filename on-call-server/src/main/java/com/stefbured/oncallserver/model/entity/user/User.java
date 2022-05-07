package com.stefbured.oncallserver.model.entity.user;

import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.role.Permission;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.stefbured.oncallserver.model.ModelConstants.User.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
public class User {
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

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "avatar_thumbnail_url")
    private String avatarThumbnailUrl;

    @Column(name = "medium_avatar_url")
    private String mediumAvatarUrl;

    @Column(name = "delete_avatar_url")
    private String deleteAvatarUrl;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "registration_date_time", nullable = false)
    private LocalDateTime registrationDateTime;

    @Column(name = "last_visit_date_time")
    private LocalDateTime lastVisitDateTime;

    @Column(name = "password_expiration_date")
    private LocalDateTime passwordExpirationDate;

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
    @OneToMany(mappedBy = "user")
    private List<UserGrant> grants;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "creator")
    private Set<Group> createdGroups;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private Set<ScheduleRecord> assignedScheduleRecords;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "creator")
    private Set<ScheduleRecord> createdScheduleRecords;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "creator")
    private Set<Chat> createdChats;

    public Boolean isBanned() {
        return isBanned;
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public Set<String> getAuthorityNames() {
        return getGrants().stream()
                .flatMap(grant -> grant.getRole().getPermissions().stream())
                .map(Permission::getAuthority)
                .collect(Collectors.toSet());
    }
}
