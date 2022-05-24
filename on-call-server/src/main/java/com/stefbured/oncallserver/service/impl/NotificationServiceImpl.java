package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.dto.NotificationDTO;
import com.stefbured.oncallserver.model.entity.notification.Notification;
import com.stefbured.oncallserver.model.entity.notification.NotificationType;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.NotificationRepository;
import com.stefbured.oncallserver.service.NotificationService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import static com.stefbured.oncallserver.OnCallConstants.NON_ACTIVE_NOTIFICATION_LIFE_TIME_DAYS;
import static com.stefbured.oncallserver.mapper.NotificationModelMapper.NOTIFICATION_MODEL_MAPPER;
import static com.stefbured.oncallserver.mapper.NotificationModelMapper.NOTIFICATION_TO_FULL_DTO;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger LOGGER = LogManager.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;
    private final OnCallModelMapper notificationMapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   SimpMessagingTemplate simpMessagingTemplate,
                                   LongPrimaryKeyGenerator primaryKeyGenerator,
                                   @Qualifier(NOTIFICATION_MODEL_MAPPER) OnCallModelMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.primaryKeyGenerator = primaryKeyGenerator;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Notification createNotification(Long creatorId,
                                           Long sourceObjectId,
                                           String notificationText,
                                           Long notificationTypeId,
                                           Long targetUserId) {
        var notification = new Notification();
        notification.setId(primaryKeyGenerator.generatePk(Notification.class));
        notification.setCreationDate(LocalDateTime.now());
        notification.setIsActive(true);
        notification.setSourceObjectId(sourceObjectId);
        var creator = new User();
        creator.setId(creatorId);
        notification.setCreator(creator);
        notification.setNotificationText(notificationText);
        var notificationType = new NotificationType();
        notificationType.setId(notificationTypeId);
        notification.setNotificationType(notificationType);
        var user = new User();
        user.setId(targetUserId);
        notification.setTargetUser(user);
        var savedNotification = notificationRepository.save(notification);
        var notificationDto = notificationMapper.map(savedNotification, NotificationDTO.class, NOTIFICATION_TO_FULL_DTO);
        simpMessagingTemplate.convertAndSendToUser(targetUserId.toString(), "/notification", notificationDto);
        return savedNotification;
    }

    @Override
    public Collection<Notification> getAllNotificationsForUser(Long userId) {
        return notificationRepository.findAllByTargetUserIdOrderByCreationDateDesc(userId);
    }

    @Override
    public Notification editNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElseThrow();
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
