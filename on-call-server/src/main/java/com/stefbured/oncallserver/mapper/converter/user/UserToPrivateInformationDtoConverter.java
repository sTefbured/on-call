package com.stefbured.oncallserver.mapper.converter.user;

import com.stefbured.oncallserver.mapper.util.OnCallMappingContext;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.role.UserGrantDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.stefbured.oncallserver.mapper.GroupModelMapper.GROUP_TO_PREVIEW_DTO;
import static com.stefbured.oncallserver.mapper.UserGrantModelMapper.USER_GRANT_TO_SHORT_DTO_FOR_USER;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PRIVATE_INFORMATION_DTO;

@Component(USER_TO_PRIVATE_INFORMATION_DTO)
public class UserToPrivateInformationDtoConverter implements Converter<User, UserDTO> {
    private Converter<UserGrant, UserGrantDTO> userGrantToShortDtoForUserConverter;
    private Converter<Group, GroupDTO> groupToPreviewDtoConverter;

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
        destination.setLastVisitDateTime(source.getLastVisitDateTime());
        destination.setPasswordExpirationDate(source.getPasswordExpirationDate());
        destination.setIsBanned(source.isBanned());
        destination.setIsEnabled(source.isEnabled());
        if (source.getGrants() != null) {
            destination.setGrants(source.getGrants().stream()
                    .map(grant -> {
                        var grantContext = new OnCallMappingContext<UserGrant, UserGrantDTO>(grant);
                        return userGrantToShortDtoForUserConverter.convert(grantContext);
                    })
                    .toList());
        }
        if (source.getCreatedGroups() != null) {
            destination.setCreatedGroups(source.getCreatedGroups().stream()
                    .map(group -> {
                        var groupContext = new OnCallMappingContext<Group, GroupDTO>(group);
                        return groupToPreviewDtoConverter.convert(groupContext);
                    })
                    .collect(Collectors.toSet()));
        }
        //TODO: add schedule records and created chats
        return destination;
    }

    @Autowired
    @Qualifier(USER_GRANT_TO_SHORT_DTO_FOR_USER)
    public void setUserGrantToShortDtoForUserConverter(Converter<UserGrant, UserGrantDTO> converter) {
        this.userGrantToShortDtoForUserConverter = converter;
    }

    @Autowired
    @Qualifier(GROUP_TO_PREVIEW_DTO)
    public void setGroupToPreviewDtoConverter(Converter<Group, GroupDTO> converter) {
        this.groupToPreviewDtoConverter = converter;
    }
}
