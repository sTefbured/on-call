package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.dto.videoconferenceroom.VideoconferenceRoomDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.vidoconferenceroom.VideoconferenceRoom;
import com.stefbured.oncallserver.service.VideoconferenceRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.stefbured.oncallserver.mapper.VideoconferenceRoomModelMapper.VIDEOCONFERENCE_ROOM_MODEL_MAPPER;
import static com.stefbured.oncallserver.mapper.VideoconferenceRoomModelMapper.VIDEOCONFERENCE_ROOM_TO_DTO;

@RestController
@RequestMapping("api/v1/videoconference")
public class VideoconferenceRoomController {
    private final VideoconferenceRoomService videoconferenceRoomService;
    private final OnCallModelMapper videoconferenceRoomMapper;

    @Autowired
    public VideoconferenceRoomController(VideoconferenceRoomService videoconferenceRoomService,
                                         @Qualifier(VIDEOCONFERENCE_ROOM_MODEL_MAPPER) OnCallModelMapper videoconferenceRoomMapper) {
        this.videoconferenceRoomService = videoconferenceRoomService;
        this.videoconferenceRoomMapper = videoconferenceRoomMapper;
    }

    @GetMapping("user/{userId}")
    @PreAuthorize("#userId.equals(authentication.details)")
    public ResponseEntity<Collection<VideoconferenceRoomDTO>> getVideoconferenceRoomsForUser(@PathVariable Long userId,
                                                                                             @RequestParam int page,
                                                                                             @RequestParam int pageSize) {
        var rooms = videoconferenceRoomService.getAllForUser(userId, page, pageSize);
        var result = videoconferenceRoomMapper.mapCollection(rooms, VideoconferenceRoomDTO.class, VIDEOCONFERENCE_ROOM_TO_DTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("{roomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VideoconferenceRoomDTO> getVideoconferenceRoomById(@PathVariable Long roomId,
                                                                             @RequestBody String accessCode) {
        var room = videoconferenceRoomService.getById(roomId);
        if (!accessCode.equals(room.getAccessCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        var result = videoconferenceRoomMapper.map(room, VideoconferenceRoomDTO.class, VIDEOCONFERENCE_ROOM_TO_DTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<VideoconferenceRoomDTO> createVideoconferenceRoom(@RequestBody VideoconferenceRoomDTO videoconferenceRoom) {
        var videoconferenceRoomEntity = new VideoconferenceRoom();
        videoconferenceRoomMapper.mapSkippingNullValues(videoconferenceRoom, videoconferenceRoomEntity);
        var creator = new User();
        creator.setId((Long) SecurityContextHolder.getContext().getAuthentication().getDetails());
        videoconferenceRoomEntity.setCreator(creator);
        var createdRoom = videoconferenceRoomService.create(videoconferenceRoomEntity);
        var result = videoconferenceRoomMapper.map(createdRoom, VideoconferenceRoomDTO.class, VIDEOCONFERENCE_ROOM_TO_DTO);
        return ResponseEntity.ok(result);
    }
}
