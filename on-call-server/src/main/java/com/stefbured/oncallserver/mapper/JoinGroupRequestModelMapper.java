package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.group.JoinGroupRequestDTO;
import com.stefbured.oncallserver.model.entity.group.JoinGroupRequest;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(JoinGroupRequestModelMapper.JOIN_GROUP_REQUEST_MODEL_MAPPER)
public class JoinGroupRequestModelMapper extends OnCallModelMapper {
    public static final String JOIN_GROUP_REQUEST_MODEL_MAPPER = "joinGroupRequestModelMapper";

    public static final String JOIN_GROUP_REQUEST_TO_FULL_DTO = "joinGroupRequestToFullDto";

    @Autowired
    @Qualifier(JOIN_GROUP_REQUEST_TO_FULL_DTO)
    public void setJoinGroupRequestModelMapper(Converter<JoinGroupRequest, JoinGroupRequestDTO> converter) {
        createTypeMap(JoinGroupRequest.class, JoinGroupRequestDTO.class, JOIN_GROUP_REQUEST_TO_FULL_DTO).setConverter(converter);
    }
}
