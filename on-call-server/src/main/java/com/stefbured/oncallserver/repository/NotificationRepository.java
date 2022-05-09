package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteAllByIsActiveFalseAndStatusChangeDateBefore(LocalDateTime statusChangeDate);
}
