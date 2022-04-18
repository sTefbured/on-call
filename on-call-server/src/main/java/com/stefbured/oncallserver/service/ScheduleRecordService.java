package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import com.stefbured.oncallserver.model.entity.user.User;

import java.util.List;

public interface ScheduleRecordService {
    List<ScheduleRecord> getAllScheduleRecordsForUser(User user, int page, int pageSize);
    List<ScheduleRecord> getAllScheduleRecordsForGroup();
    ScheduleRecord createScheduleRecord(ScheduleRecord scheduleRecord);
    ScheduleRecord editScheduleRecord(ScheduleRecord scheduleRecord);
    void deleteScheduleRecord(ScheduleRecord scheduleRecord);
}
