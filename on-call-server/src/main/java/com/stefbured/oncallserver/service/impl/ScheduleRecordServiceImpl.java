package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.repository.ScheduleRecordRepository;
import com.stefbured.oncallserver.service.ScheduleRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleRecordServiceImpl implements ScheduleRecordService {
    private final ScheduleRecordRepository scheduleRecordRepository;

    @Autowired
    public ScheduleRecordServiceImpl(ScheduleRecordRepository scheduleRecordRepository) {
        this.scheduleRecordRepository = scheduleRecordRepository;
    }

    @Override
    public List<ScheduleRecord> getAllScheduleRecordsForUser(User user, int page, int pageSize) {
        return null;
    }

    @Override
    public List<ScheduleRecord> getAllScheduleRecordsForGroup() {
        return null;
    }

    @Override
    public ScheduleRecord createScheduleRecord(ScheduleRecord scheduleRecord) {
        return null;
    }

    @Override
    public ScheduleRecord editScheduleRecord(ScheduleRecord scheduleRecord) {
        return null;
    }

    @Override
    public void deleteScheduleRecord(ScheduleRecord scheduleRecord) {

    }
}
