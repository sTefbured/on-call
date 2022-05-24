package com.stefbured.oncallserver.model.entity.notification;

import com.stefbured.oncallserver.model.entity.user.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    private Long id;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "status_change_date")
    private LocalDateTime statusChangeDate;

    @Column(name = "is_active")
    @Type(type = "boolean")
    @Getter(AccessLevel.NONE)
    private Boolean isActive;

    @Column(name = "notification_text")
    private String notificationText;

    @Column(name = "source_object_id")
    private Long sourceObjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_type_id")
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    public Boolean isActive() {
        return isActive;
    }
}
