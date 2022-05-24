package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.notification.Notification;

import java.util.Collection;

public interface NotificationService {
    Notification createNotification(Long creatorId, Long sourceObjectId,
                                    String notificationText, Long notificationTypeId, Long targetUserId);
    Collection<Notification> getAllNotificationsForUser(Long userId);
    Notification editNotification(Notification notification);
    Notification getNotificationById(Long id);
    void deleteExpiredNonActiveNotifications();
}
