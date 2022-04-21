package com.stefbured.oncallserver.mapper.converter.user;

import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PUBLIC_INFORMATION_DTO;

@Component(USER_TO_PUBLIC_INFORMATION_DTO)
public class UserToPublicInformationDtoConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(MappingContext<User, UserDTO> context) {
        var source = context.getSource();
        var destination = new UserDTO();
        destination.setId(source.getId());
        destination.setUsername(source.getUsername());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setBirthDate(source.getBirthDate());
        destination.setRegistrationDateTime(source.getRegistrationDateTime());
        destination.setLastVisitDateTime(source.getLastVisitDateTime());
        destination.setIsBanned(source.isBanned());
        return destination;
    }
}
