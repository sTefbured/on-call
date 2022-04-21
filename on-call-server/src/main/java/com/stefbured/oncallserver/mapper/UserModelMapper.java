package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(UserModelMapper.USER_MODEL_MAPPER)
public class UserModelMapper extends OnCallModelMapper {
    public static final String USER_MODEL_MAPPER = "userModelMapper";

    public static final String USER_TO_PRIVATE_INFORMATION_DTO = "userToPrivateInformationDto";
    public static final String USER_TO_PUBLIC_INFORMATION_DTO = "userToPublicInformationDto";
    public static final String USER_TO_PREVIEW_DTO = "userToPreviewDto";
    public static final String USER_TO_POST_REGISTRATION_DTO = "userToPostRegistrationDto";

    @Autowired
    @Qualifier(USER_TO_PREVIEW_DTO)
    private void setUserToPreviewDtoConverter(Converter<User, UserDTO> converter) {
        createTypeMap(User.class, UserDTO.class, USER_TO_PREVIEW_DTO).setConverter(converter);
    }

    @Autowired
    @Qualifier(USER_TO_PRIVATE_INFORMATION_DTO)
    private void setUserToPrivateInformationDtoConverter(Converter<User, UserDTO> converter) {
        createTypeMap(User.class, UserDTO.class, USER_TO_PRIVATE_INFORMATION_DTO).setConverter(converter);
    }

    @Autowired
    @Qualifier(USER_TO_PUBLIC_INFORMATION_DTO)
    private void setUserToPublicInformationDtoConverter(Converter<User, UserDTO> converter) {
        createTypeMap(User.class, UserDTO.class, USER_TO_PUBLIC_INFORMATION_DTO).setConverter(converter);
    }

    @Autowired
    @Qualifier(USER_TO_POST_REGISTRATION_DTO)
    private void setUserToPostRegistrationDtoConverter(Converter<User, UserDTO> converter) {
        createTypeMap(User.class, UserDTO.class, USER_TO_POST_REGISTRATION_DTO).setConverter(converter);
    }
}
