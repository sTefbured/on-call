package com.stefbured.oncallserver.service;

import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ScheduleRecordService {
    ScheduleRecord getScheduleRecordById(Long recordId);
    Collection<ScheduleRecord> getAllScheduleRecordsForUser(Long userId, LocalDateTime from, LocalDateTime to);
    Collection<ScheduleRecord> getAllScheduleRecordsForGroup(Long groupId, LocalDateTime from, LocalDateTime to);
    ScheduleRecord createScheduleRecord(ScheduleRecord scheduleRecord);
    ScheduleRecord editScheduleRecord(ScheduleRecord scheduleRecord);
    void deleteScheduleRecordById(Long recordId);
    void sendNotificationsForActiveEvents();
}
