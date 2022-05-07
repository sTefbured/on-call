package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.role.UserGrantDTO;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(UserGrantModelMapper.USER_GRANT_MODEL_MAPPER)
public class UserGrantModelMapper extends OnCallModelMapper {
    public static final String USER_GRANT_MODEL_MAPPER = "userGrantModelMapper";

    public static final String USER_GRANT_TO_SHORT_DTO_FOR_USER = "userGrantToShortDtoForUser";

    @Autowired
    @Qualifier(USER_GRANT_TO_SHORT_DTO_FOR_USER)
    public void setUserGrantToShortDtoForUserConverter(Converter<UserGrant, UserGrantDTO> converter) {
        createTypeMap(UserGrant.class, UserGrantDTO.class, USER_GRANT_TO_SHORT_DTO_FOR_USER).setConverter(converter);
    }
}
