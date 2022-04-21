package com.stefbured.oncallserver.mapper.converter.usergrant;

import com.stefbured.oncallserver.mapper.util.OnCallMappingContext;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.role.UserGrantDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.role.UserGrant;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.GroupModelMapper.GROUP_TO_PREVIEW_DTO;
import static com.stefbured.oncallserver.mapper.UserGrantModelMapper.USER_GRANT_TO_SHORT_DTO_FOR_USER;

@Component(USER_GRANT_TO_SHORT_DTO_FOR_USER)
public class UserGrantToShortDtoForUserConverter implements Converter<UserGrant, UserGrantDTO> {
    private Converter<Group, GroupDTO> groupToPreviewConverter;

    @Override
    public UserGrantDTO convert(MappingContext<UserGrant, UserGrantDTO> context) {
        var source = context.getSource();
        var destination = new UserGrantDTO();
        destination.setId(source.getId());
        if (source.getGroup() != null) {
            var groupContext = new OnCallMappingContext<Group, GroupDTO>(source.getGroup());
            destination.setGroup(groupToPreviewConverter.convert(groupContext));
        }
        if (source.getChat() != null) {
//                        TODO: add chat mapping after its implementation
//                        var typeMapper = groupMapper.getTypeMap(User.class, UserDTO.class, USER_TO_PREVIEW_DTO);
//                        destination.setUser(typeMapper.map(source.getUser()));
        }

//                    TODO: add role mapping after its implementation
//                    var roleMapper = roleMapper.getTypeMap(Role.class, RoleDTO.class, USER_TO_PREVIEW_DTO);
//                    destination.setRole(roleMapper.map(source.getRole()));
        return destination;
    }

    @Autowired
    @Qualifier(GROUP_TO_PREVIEW_DTO)
    public void setGroupToPreviewConverter(Converter<Group, GroupDTO> groupToPreviewConverter) {
        this.groupToPreviewConverter = groupToPreviewConverter;
    }
}