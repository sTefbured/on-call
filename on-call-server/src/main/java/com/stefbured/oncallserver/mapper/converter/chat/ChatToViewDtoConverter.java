package com.stefbured.oncallserver.mapper.converter.chat;

import com.stefbured.oncallserver.mapper.util.OnCallMappingContext;
import com.stefbured.oncallserver.model.dto.chat.ChatDTO;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.ChatModelMapper.CHAT_TO_VIEW_DTO;
import static com.stefbured.oncallserver.mapper.GroupModelMapper.GROUP_TO_PREVIEW_DTO;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PREVIEW_DTO;

@Component(CHAT_TO_VIEW_DTO)
public class ChatToViewDtoConverter implements Converter<Chat, ChatDTO> {
    private Converter<Group, GroupDTO> groupToPreviewDtoConverter;
    private Converter<User, UserDTO> userToPreviewDtoConverter;

    @Override
    public ChatDTO convert(MappingContext<Chat, ChatDTO> context) {
        var source = context.getSource();
        var destination = new ChatDTO();
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setCreationDateTime(source.getCreationDateTime());
        var creatorContext = new OnCallMappingContext<User, UserDTO>(source.getCreator());
        destination.setCreator(userToPreviewDtoConverter.convert(creatorContext));
        if (source.getGroup() != null) {
            var groupContext = new OnCallMappingContext<Group, GroupDTO>(source.getGroup());
            destination.setGroup(groupToPreviewDtoConverter.convert(groupContext));
        }
        return destination;
    }

    @Autowired
    @Qualifier(GROUP_TO_PREVIEW_DTO)
    public void setGroupToPreviewDtoConverter(Converter<Group, GroupDTO> converter) {
        this.groupToPreviewDtoConverter = converter;
    }

    @Autowired
    @Qualifier(USER_TO_PREVIEW_DTO)
    public void setUserToPreviewDtoConverter(Converter<User, UserDTO> converter) {
        this.userToPreviewDtoConverter = converter;
    }
}
