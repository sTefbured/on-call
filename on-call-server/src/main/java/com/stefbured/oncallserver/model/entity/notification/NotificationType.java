package com.stefbured.oncallserver.model.entity.notification;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "notification_types")
public class NotificationType {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;
}
