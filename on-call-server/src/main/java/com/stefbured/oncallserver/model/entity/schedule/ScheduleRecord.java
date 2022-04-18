package com.stefbured.oncallserver.model.entity.schedule;

import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.user.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * ScheduleRecord represents a single event in schedule. The record can either be owned by a user or by a group.
 * User-level schedule record (case when {@code user_id} is filled and {@code user_group_id} is not) is only available
 * for the owner and for privileged users (administrator, etc.).
 * Group-level schedule record ({@code user_group_id} is filled and {@code user_id} is empty) is available for
 *
 */
@Data
@Entity
@Table(name = "schedule_records")
public class ScheduleRecord {
    @Id
    private Long id;

    @Column(name = "event_date_time")
    private LocalDateTime eventDateTime;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;
}
