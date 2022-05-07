package com.stefbured.oncallserver.mapper.converter.chat;

import com.stefbured.oncallserver.model.dto.chat.ChatDTO;
import com.stefbured.oncallserver.model.entity.chat.Chat;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.ChatModelMapper.CHAT_TO_ID_DTO;

@Component(CHAT_TO_ID_DTO)
public class ChatToIdDtoConverter implements Converter<Chat, ChatDTO> {
    @Override
    public ChatDTO convert(MappingContext<Chat, ChatDTO> context) {
        var source = context.getSource();
        var destination = new ChatDTO();
        destination.setId(source.getId());
        return destination;
    }
}
