package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.notification.Notification;

public interface NotificationService {
    Notification createNotification(Long sourceId, Long targetId, Long notificationTypeId, Long targetUserId);
    void deleteExpiredNonActiveNotifications();
}
