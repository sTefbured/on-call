package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.chat.MessageDTO;
import com.stefbured.oncallserver.model.entity.chat.Message;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(MessageModelMapper.MESSAGE_MODEL_MAPPER)
public class MessageModelMapper extends OnCallModelMapper {
    public static final String MESSAGE_MODEL_MAPPER = "messageModelMapper";

    public static final String MESSAGE_TO_FULL_DTO = "messageToFullDto";

    @Autowired
    @Qualifier(MESSAGE_TO_FULL_DTO)
    public void setMessageToFullDtoConverter(Converter<Message, MessageDTO> converter) {
        createTypeMap(Message.class, MessageDTO.class, MESSAGE_TO_FULL_DTO).setConverter(converter);
    }
}
