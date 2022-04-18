package com.stefbured.oncallserver.model.entity.group;

import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import com.stefbured.oncallserver.model.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.Group.*;

@Entity
@Data
@Table(name = "groups")
public class Group {
    @Id
    private Long id;

    @Column(name = "id_tag", length = MAX_GROUP_ID_TAG_LENGTH)
    private String idTag;

    @Column(name = "name", length = MAX_GROUP_NAME_LENGTH, nullable = false)
    private String name;

    @Column(name = "description", length = MAX_GROUP_DESCRIPTION_LENGTH)
    private String description;

    @Column(name = "creation_date_time", nullable = false)
    private LocalDateTime creationDateTime;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_group_id")
    private Group parentGroup;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "group")
    private Set<UserGrant> userGrants;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "parentGroup")
    private Set<Group> childGroups;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "group")
    private Set<ScheduleRecord> scheduleRecords;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "group")
    private Set<Chat> chats;
}
