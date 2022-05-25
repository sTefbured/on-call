package com.stefbured.oncallserver.mapper.converter.group;

import com.stefbured.oncallserver.mapper.util.OnCallMappingContext;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.stefbured.oncallserver.mapper.GroupModelMapper.GROUP_TO_MEMBER_VIEW_DTO;
import static com.stefbured.oncallserver.mapper.GroupModelMapper.GROUP_TO_PREVIEW_DTO;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PREVIEW_DTO;

@Component(GROUP_TO_MEMBER_VIEW_DTO)
public class GroupToMemberViewDtoConverter implements Converter<Group, GroupDTO> {
    private Converter<Group, GroupDTO> groupToPreviewDtoConverter;
    private Converter<User, UserDTO> userToPreviewDtoConverter;

    @Override
    public GroupDTO convert(MappingContext<Group, GroupDTO> context) {
        var source = context.getSource();
        GroupDTO parentGroup = null;
        if (source.getParentGroup() != null) {
            var previewContext = new OnCallMappingContext<Group, GroupDTO>(source.getParentGroup());
            parentGroup = groupToPreviewDtoConverter.convert(previewContext);
        }
        Set<GroupDTO> childGroups = null;
        if (source.getChildGroups() != null) {
            childGroups = source.getChildGroups().stream()
                    .map(group -> {
                        var groupContext = new OnCallMappingContext<Group, GroupDTO>(group);
                        return groupToPreviewDtoConverter.convert(groupContext);
                    })
                    .collect(Collectors.toSet());
        }
        var userContext = new OnCallMappingContext<User, UserDTO>(source.getCreator());
        var creator = userToPreviewDtoConverter.convert(userContext);
        var destination = new GroupDTO();
        destination.setId(source.getId());
        destination.setIdTag(source.getIdTag());
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        destination.setCreationDateTime(source.getCreationDateTime());
        destination.setCreator(creator);
        destination.setParentGroup(parentGroup);
        destination.setChildGroups(childGroups);
        destination.setAvatarUrl(source.getAvatarUrl());
        destination.setMediumAvatarUrl(source.getMediumAvatarUrl());
        destination.setAvatarThumbnailUrl(source.getAvatarThumbnailUrl());
        return destination;
    }

    @Autowired
    @Qualifier(GROUP_TO_PREVIEW_DTO)
    public void setGroupToPreviewDtoConverter(Converter<Group, GroupDTO> groupToPreviewDtoConverter) {
        this.groupToPreviewDtoConverter = groupToPreviewDtoConverter;
    }

    @Autowired
    @Qualifier(USER_TO_PREVIEW_DTO)
    public void setUserToPreviewDtoConverter(Converter<User, UserDTO> userToPreviewDtoConverter) {
        this.userToPreviewDtoConverter = userToPreviewDtoConverter;
    }
}
