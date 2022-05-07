package com.stefbured.oncallserver.mapper.converter.schedulerecord;

import com.stefbured.oncallserver.mapper.util.OnCallMappingContext;
import com.stefbured.oncallserver.model.dto.ScheduleRecordDTO;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.group.Group;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import com.stefbured.oncallserver.model.entity.user.User;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.GroupModelMapper.GROUP_TO_PREVIEW_DTO;
import static com.stefbured.oncallserver.mapper.ScheduleRecordModelMapper.SCHEDULE_RECORD_TO_FULL_DTO;
import static com.stefbured.oncallserver.mapper.UserModelMapper.USER_TO_PREVIEW_DTO;

@Component(SCHEDULE_RECORD_TO_FULL_DTO)
public class ScheduleRecordToFullDtoConverter implements Converter<ScheduleRecord, ScheduleRecordDTO> {
    private Converter<User, UserDTO> userToPreviewDtoConverter;
    private Converter<Group, GroupDTO> groupToPreviewDtoConverter;

    @Override
    public ScheduleRecordDTO convert(MappingContext<ScheduleRecord, ScheduleRecordDTO> context) {
        var source = context.getSource();
        var destination = new ScheduleRecordDTO();
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        destination.setEventDateTime(source.getEventDateTime());
        destination.setCreationDateTime(source.getCreationDateTime());
        if (source.getUser() != null) {
            var userContext = new OnCallMappingContext<User, UserDTO>(source.getUser());
            destination.setUser(userToPreviewDtoConverter.convert(userContext));
        }
        if (source.getGroup() != null) {
            var groupContext = new OnCallMappingContext<Group, GroupDTO>(source.getGroup());
            destination.setGroup(groupToPreviewDtoConverter.convert(groupContext));
        }
        if (source.getCreator() != null) {
            var userContext = new OnCallMappingContext<User, UserDTO>(source.getCreator());
            destination.setCreator(userToPreviewDtoConverter.convert(userContext));
        }
        return destination;
    }

    @Autowired
    @Qualifier(USER_TO_PREVIEW_DTO)
    public void setUserToPreviewDtoConverter(Converter<User, UserDTO> converter) {
        this.userToPreviewDtoConverter = converter;
    }

    @Autowired
    @Qualifier(GROUP_TO_PREVIEW_DTO)
    public void setGroupToPreviewDtoConverter(Converter<Group, GroupDTO> converter) {
        this.groupToPreviewDtoConverter = converter;
    }
}
