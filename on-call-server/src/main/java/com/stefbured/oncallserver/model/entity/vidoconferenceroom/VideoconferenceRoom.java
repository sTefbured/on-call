package com.stefbured.oncallserver.model.entity.vidoconferenceroom;

import com.stefbured.oncallserver.model.entity.user.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "videoconference_rooms")
public class VideoconferenceRoom {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "access_code")
    private String accessCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
}
