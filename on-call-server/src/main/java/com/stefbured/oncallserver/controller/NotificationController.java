package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.dto.NotificationDTO;
import com.stefbured.oncallserver.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;

import static com.stefbured.oncallserver.mapper.NotificationModelMapper.NOTIFICATION_MODEL_MAPPER;
import static com.stefbured.oncallserver.mapper.NotificationModelMapper.NOTIFICATION_TO_FULL_DTO;

@RestController
@RequestMapping("api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final OnCallModelMapper notificationMapper;

    @Autowired
    public NotificationController(NotificationService notificationService,
                                  @Qualifier(NOTIFICATION_MODEL_MAPPER) OnCallModelMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("{userId}")
    @PreAuthorize("#userId.equals(authentication.details)")
    public ResponseEntity<Collection<NotificationDTO>> getAllNotifications(@PathVariable Long userId) {
        var notifications = notificationService.getAllNotificationsForUser(userId);
        var result = notificationMapper.mapCollection(notifications, NotificationDTO.class, NOTIFICATION_TO_FULL_DTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("read")
    @PreAuthorize("authentication.getDetails().equals(#notification?.targetUser?.id)")
    public ResponseEntity<NotificationDTO> readNotification(@RequestBody NotificationDTO notification) {
        var queriedNotification = notificationService.getNotificationById(notification.getId());
        if (!notification.getTargetUser().getId().equals(queriedNotification.getTargetUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        queriedNotification.setIsActive(false);
        queriedNotification.setStatusChangeDate(LocalDateTime.now());
        var editedNotification = notificationService.editNotification(queriedNotification);
        var result = notificationMapper.map(editedNotification, NotificationDTO.class, NOTIFICATION_TO_FULL_DTO);
        return ResponseEntity.ok(result);
    }
}
