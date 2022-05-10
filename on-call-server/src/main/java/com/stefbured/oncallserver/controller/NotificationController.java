package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.entity.notification.Notification;
import com.stefbured.oncallserver.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("{userId}")
    @PreAuthorize("#userId.equals(authentication.details)")
    public ResponseEntity<Collection<Notification>> getAllNotifications(@PathVariable Long userId) {
        var notifications = notificationService.getAllNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }
}
