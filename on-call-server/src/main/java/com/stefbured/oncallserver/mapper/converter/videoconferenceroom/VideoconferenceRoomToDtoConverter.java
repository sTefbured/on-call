package com.stefbured.oncallserver.mapper.converter.videoconferenceroom;

import com.stefbured.oncallserver.mapper.util.OnCallMappingContext;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.dto.videoconferenceroom.VideoconferenceRoomDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.vidoconferenceroom.VideoconferenceRoom;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PREVIEW_DTO;
import static com.stefbured.oncallserver.mapper.VideoconferenceRoomModelMapper.VIDEOCONFERENCE_ROOM_TO_DTO;

@Component(VIDEOCONFERENCE_ROOM_TO_DTO)
public class VideoconferenceRoomToDtoConverter implements Converter<VideoconferenceRoom, VideoconferenceRoomDTO> {
    private Converter<User, UserDTO> userToPreviewDtoConverter;

    @Override
    public VideoconferenceRoomDTO convert(MappingContext<VideoconferenceRoom, VideoconferenceRoomDTO> context) {
        var source = context.getSource();
        var destination = new VideoconferenceRoomDTO();
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setAccessCode(source.getAccessCode());
        if (source.getCreator() != null) {
            var creatorContext = new OnCallMappingContext<User, UserDTO>(source.getCreator());
            destination.setCreator(userToPreviewDtoConverter.convert(creatorContext));
        }
        return destination;
    }

    @Autowired
    @Qualifier(USER_TO_PREVIEW_DTO)
    public void setUserToPreviewDtoConverter(Converter<User, UserDTO> converter) {
        this.userToPreviewDtoConverter = converter;
    }
}
