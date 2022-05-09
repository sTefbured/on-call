package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.notification.Notification;
import com.stefbured.oncallserver.model.entity.notification.NotificationType;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.NotificationRepository;
import com.stefbured.oncallserver.service.NotificationService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.stefbured.oncallserver.OnCallConstants.NON_ACTIVE_NOTIFICATION_LIFE_TIME_DAYS;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger LOGGER = LogManager.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.notificationRepository = notificationRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Override
    public Notification createNotification(Long sourceId, Long targetId, Long notificationTypeId, Long targetUserId) {
        var notification = new Notification();
        notification.setId(primaryKeyGenerator.generatePk(Notification.class));
        notification.setCreationDate(LocalDateTime.now());
        notification.setIsActive(true);
        notification.setSourceId(sourceId);
        notification.setTargetId(targetId);
        var notificationType = new NotificationType();
        notificationType.setId(notificationTypeId);
        notification.setNotificationType(notificationType);
        var user = new User();
        user.setId(targetUserId);
        notification.setTargetUser(user);
        return notificationRepository.save(notification);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredNonActiveNotifications() {
        var statusChangeDate = LocalDateTime.now()
                .minusDays(NON_ACTIVE_NOTIFICATION_LIFE_TIME_DAYS)
                .truncatedTo(ChronoUnit.DAYS);
        LOGGER.info("Deleting non-active notifications which changed status before {}", statusChangeDate);
        notificationRepository.deleteAllByIsActiveFalseAndStatusChangeDateBefore(statusChangeDate);
    }
}
