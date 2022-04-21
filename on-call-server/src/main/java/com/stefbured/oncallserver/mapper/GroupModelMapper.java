package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(GroupModelMapper.GROUP_MODEL_MAPPER)
public class GroupModelMapper extends OnCallModelMapper {
    public static final String GROUP_MODEL_MAPPER = "groupModelMapper";

    public static final String GROUP_TO_MEMBER_VIEW_DTO = "groupToMemberViewDto";
    public static final String GROUP_TO_ADMIN_VIEW_DTO = "groupToAdminViewDto";
    public static final String GROUP_TO_PREVIEW_DTO = "groupToPreviewDto";

    @Autowired
    @Qualifier(GROUP_TO_MEMBER_VIEW_DTO)
    public void setGroupToMemberViewDtoConverter(Converter<Group, GroupDTO> converter) {
        createTypeMap(Group.class, GroupDTO.class, GROUP_TO_MEMBER_VIEW_DTO).setConverter(converter);
    }

    @Autowired
    @Qualifier(GROUP_TO_PREVIEW_DTO)
    public void setGroupToPreviewDtoConverter(Converter<Group, GroupDTO> converter) {
        createTypeMap(Group.class, GroupDTO.class, GROUP_TO_PREVIEW_DTO).setConverter(converter);
    }

    @Autowired
    @Qualifier(GROUP_TO_PREVIEW_DTO)
    public void setGroupToAdminViewDtoConverter(Converter<Group, GroupDTO> converter) {
        createTypeMap(Group.class, GroupDTO.class, GROUP_TO_ADMIN_VIEW_DTO).setConverter(converter);
    }
}
