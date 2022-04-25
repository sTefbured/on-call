package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.chat.ChatDTO;
import com.stefbured.oncallserver.model.entity.chat.Chat;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(ChatModelMapper.CHAT_MODEL_MAPPER)
public class ChatModelMapper extends OnCallModelMapper {
    public static final String CHAT_MODEL_MAPPER = "chatModelMapper";

    public static final String CHAT_TO_VIEW_DTO = "chatToFullDto";
    public static final String CHAT_TO_ID_DTO = "chatToIdDto";

    @Autowired
    @Qualifier(CHAT_TO_VIEW_DTO)
    public void setChatToViewDtoConverter(Converter<Chat, ChatDTO> converter) {
        createTypeMap(Chat.class, ChatDTO.class, CHAT_TO_VIEW_DTO).setConverter(converter);
    }

    @Autowired
    @Qualifier(CHAT_TO_ID_DTO)
    public void setChatToIdDtoConverter(Converter<Chat, ChatDTO> converter) {
        createTypeMap(Chat.class, ChatDTO.class, CHAT_TO_ID_DTO).setConverter(converter);
    }
}
