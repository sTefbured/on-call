package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.vidoconferenceroom.VideoconferenceRoom;
import com.stefbured.oncallserver.repository.VideoconferenceRoomRepository;
import com.stefbured.oncallserver.service.VideoconferenceRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class VideoconferenceRoomServiceImpl implements VideoconferenceRoomService {
    private final VideoconferenceRoomRepository videoconferenceRoomRepository;

    @Autowired
    public VideoconferenceRoomServiceImpl(VideoconferenceRoomRepository videoconferenceRoomRepository) {
        this.videoconferenceRoomRepository = videoconferenceRoomRepository;
    }

    @Override
    public VideoconferenceRoom getById(Long roomId) {
        return videoconferenceRoomRepository.findById(roomId).orElseThrow();
    }

    @Override
    public VideoconferenceRoom create(VideoconferenceRoom videoconferenceRoom) {
        return videoconferenceRoomRepository.save(videoconferenceRoom);
    }

    @Override
    public Collection<VideoconferenceRoom> getAllForUser(Long userId, int page, int pageSize) {
        return videoconferenceRoomRepository.findAllByCreatorId(userId, Pageable.ofSize(pageSize).withPage(page)).toList();
    }
}
