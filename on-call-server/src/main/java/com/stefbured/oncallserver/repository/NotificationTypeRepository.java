package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.notification.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
}
