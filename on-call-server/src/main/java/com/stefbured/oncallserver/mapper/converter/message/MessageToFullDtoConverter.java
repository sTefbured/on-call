package com.stefbured.oncallserver.mapper.converter.message;

import com.stefbured.oncallserver.mapper.util.OnCallMappingContext;
import com.stefbured.oncallserver.model.dto.chat.ChatDTO;
import com.stefbured.oncallserver.model.dto.chat.MessageDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.chat.Message;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.ChatModelMapper.CHAT_TO_ID_DTO;
import static com.stefbured.oncallserver.mapper.MessageModelMapper.MESSAGE_TO_FULL_DTO;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PREVIEW_DTO;

@Component(MESSAGE_TO_FULL_DTO)
public class MessageToFullDtoConverter implements Converter<Message, MessageDTO> {
    private Converter<User, UserDTO> userToPreviewDtoConverter;
    private Converter<Chat, ChatDTO> chatToIdDtoConverter;

    @Override
    public MessageDTO convert(MappingContext<Message, MessageDTO> context) {
        var source = context.getSource();
        var destination = new MessageDTO();
        destination.setId(source.getId());
        destination.setText(source.getText());
        destination.setSendingDateTime(source.getSendingDateTime());
        var senderContext = new OnCallMappingContext<User, UserDTO>(source.getSender());
        destination.setSender(userToPreviewDtoConverter.convert(senderContext));
        var chatContext = new OnCallMappingContext<Chat, ChatDTO>(source.getChat());
        destination.setChat(chatToIdDtoConverter.convert(chatContext));
        return destination;
    }

    @Autowired
    @Qualifier(USER_TO_PREVIEW_DTO)
    public void setUserToPreviewDtoConverter(Converter<User, UserDTO> converter) {
        this.userToPreviewDtoConverter = converter;
    }

    @Autowired
    @Qualifier(CHAT_TO_ID_DTO)
    public void setChatToIdDtoConverter(Converter<Chat, ChatDTO> converter) {
        this.chatToIdDtoConverter = converter;
    }
}
