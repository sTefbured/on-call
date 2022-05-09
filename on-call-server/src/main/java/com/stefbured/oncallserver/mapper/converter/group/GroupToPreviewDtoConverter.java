package com.stefbured.oncallserver.mapper.converter.group;

import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.GroupModelMapper.GROUP_TO_PREVIEW_DTO;

@Component(GROUP_TO_PREVIEW_DTO)
public class GroupToPreviewDtoConverter implements Converter<Group, GroupDTO> {
    @Override
    public GroupDTO convert(MappingContext<Group, GroupDTO> context) {
        var source = context.getSource();
        var destination = new GroupDTO();
        destination.setId(source.getId());
        destination.setIdTag(source.getIdTag());
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        destination.setMediumAvatarUrl(source.getMediumAvatarUrl());
        destination.setAvatarThumbnailUrl(source.getAvatarThumbnailUrl());
        return destination;
    }
}
