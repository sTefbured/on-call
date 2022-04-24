package com.stefbured.oncallserver.mapper;

import com.stefbured.oncallserver.model.dto.ScheduleRecordDTO;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(ScheduleRecordModelMapper.SCHEDULE_RECORD_MODEL_MAPPER)
public class ScheduleRecordModelMapper extends OnCallModelMapper {
    public static final String SCHEDULE_RECORD_MODEL_MAPPER = "scheduleRecordModelMapper";

    public static final String SCHEDULE_RECORD_TO_FULL_DTO = "scheduleRecordToFullDto";
    public static final String SCHEDULE_RECORD_TO_PREVIEW_DTO = "scheduleRecordToPreviewDto";

    @Autowired
    @Qualifier(SCHEDULE_RECORD_TO_FULL_DTO)
    public void setScheduleRecordToFullDtoConverter(Converter<ScheduleRecord, ScheduleRecordDTO> converter) {
        createTypeMap(ScheduleRecord.class, ScheduleRecordDTO.class, SCHEDULE_RECORD_TO_FULL_DTO).setConverter(converter);
    }

    @Autowired
    @Qualifier(SCHEDULE_RECORD_TO_PREVIEW_DTO)
    public void setScheduleRecordToPreviewDtoConverter(Converter<ScheduleRecord, ScheduleRecordDTO> converter) {
        createTypeMap(ScheduleRecord.class, ScheduleRecordDTO.class, SCHEDULE_RECORD_TO_PREVIEW_DTO).setConverter(converter);
    }
}
