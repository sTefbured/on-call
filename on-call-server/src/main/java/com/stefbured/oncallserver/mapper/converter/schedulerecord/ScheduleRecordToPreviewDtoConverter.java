package com.stefbured.oncallserver.mapper.converter.schedulerecord;

import com.stefbured.oncallserver.model.dto.ScheduleRecordDTO;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import static com.stefbured.oncallserver.mapper.ScheduleRecordModelMapper.SCHEDULE_RECORD_TO_PREVIEW_DTO;

@Component(SCHEDULE_RECORD_TO_PREVIEW_DTO)
public class ScheduleRecordToPreviewDtoConverter implements Converter<ScheduleRecord, ScheduleRecordDTO> {
    @Override
    public ScheduleRecordDTO convert(MappingContext<ScheduleRecord, ScheduleRecordDTO> context) {
        var source = context.getSource();
        var destination = new ScheduleRecordDTO();
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setEventDateTime(source.getEventDateTime());
        return destination;
    }
}
