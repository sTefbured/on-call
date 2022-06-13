package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.vidoconferenceroom.VideoconferenceRoom;
import com.stefbured.oncallserver.repository.VideoconferenceRoomRepository;
import com.stefbured.oncallserver.service.VideoconferenceRoomService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class VideoconferenceRoomServiceImpl implements VideoconferenceRoomService {
    private final VideoconferenceRoomRepository videoconferenceRoomRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public VideoconferenceRoomServiceImpl(VideoconferenceRoomRepository videoconferenceRoomRepository,
                                          LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.videoconferenceRoomRepository = videoconferenceRoomRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Override
    public VideoconferenceRoom getById(Long roomId) {
        return videoconferenceRoomRepository.findById(roomId).orElseThrow();
    }

    @Override
    public VideoconferenceRoom create(VideoconferenceRoom videoconferenceRoom) {
        videoconferenceRoom.setId(primaryKeyGenerator.generatePk(VideoconferenceRoom.class));
        return videoconferenceRoomRepository.save(videoconferenceRoom);
    }

    @Override
    public Collection<VideoconferenceRoom> getAllForUser(Long userId, int page, int pageSize) {
        return videoconferenceRoomRepository.findAllByCreatorId(userId, Pageable.ofSize(pageSize).withPage(page)).toList();
    }
}
