package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.videoconferenceroom.VideoconferenceRoomDTO;
import com.stefbured.oncallserver.model.entity.vidoconferenceroom.VideoconferenceRoom;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(VideoconferenceRoomModelMapper.VIDEOCONFERENCE_ROOM_MODEL_MAPPER)
public class VideoconferenceRoomModelMapper extends OnCallModelMapper {
    public static final String VIDEOCONFERENCE_ROOM_MODEL_MAPPER = "videoconferenceRoomModelMapper";

    public static final String VIDEOCONFERENCE_ROOM_TO_DTO = "videoconferenceRoomToDto";

    @Autowired
    @Qualifier(VIDEOCONFERENCE_ROOM_TO_DTO)
    public void setVideoconferenceRoomToDtoConverter(Converter<VideoconferenceRoom, VideoconferenceRoomDTO> converter) {
        createTypeMap(VideoconferenceRoom.class, VideoconferenceRoomDTO.class, VIDEOCONFERENCE_ROOM_TO_DTO).setConverter(converter);
    }
}
