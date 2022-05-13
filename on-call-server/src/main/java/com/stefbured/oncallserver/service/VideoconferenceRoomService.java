package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.vidoconferenceroom.VideoconferenceRoom;

import java.util.Collection;

public interface VideoconferenceRoomService {
    VideoconferenceRoom getById(Long roomId);
    VideoconferenceRoom create(VideoconferenceRoom videoconferenceRoom);
    Collection<VideoconferenceRoom> getAllForUser(Long userId, int page, int pageSize);
}
