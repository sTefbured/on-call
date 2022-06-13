package com.stefbured.oncallserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.notification.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO {
    private Long id;

    private LocalDateTime creationDate;

    private LocalDateTime statusChangeDate;

    private Boolean isActive;

    private UserDTO creator;

    private String notificationText;

    private Long sourceObjectId;

    private NotificationType notificationType;

    private UserDTO targetUser;
}
