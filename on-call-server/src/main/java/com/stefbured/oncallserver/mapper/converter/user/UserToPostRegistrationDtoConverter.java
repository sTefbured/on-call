package com.stefbured.oncallserver.mapper.converter.user;

import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_POST_REGISTRATION_DTO;

@Component(USER_TO_POST_REGISTRATION_DTO)
public class UserToPostRegistrationDtoConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(MappingContext<User, UserDTO> context) {
        var source = context.getSource();
        var destination = new UserDTO();
        destination.setId(source.getId());
        destination.setUsername(source.getUsername());
        destination.setEmail(source.getEmail());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setBirthDate(source.getBirthDate());
        destination.setRegistrationDateTime(source.getRegistrationDateTime());
        destination.setPasswordExpirationDate(source.getPasswordExpirationDate());
        destination.setAvatarUrl(source.getAvatarUrl());
        return destination;
    }
}
