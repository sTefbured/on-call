package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.GroupNotFoundException;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import com.stefbured.oncallserver.repository.GroupRepository;
import com.stefbured.oncallserver.repository.ScheduleRecordRepository;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.ScheduleRecordService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

import static com.stefbured.oncallserver.mapper.ScheduleRecordModelMapper.SCHEDULE_RECORD_MODEL_MAPPER;

@Service
public class ScheduleRecordServiceImpl implements ScheduleRecordService {
    private final ScheduleRecordRepository scheduleRecordRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;
    private final OnCallModelMapper modelMapper;

    @Autowired
    public ScheduleRecordServiceImpl(ScheduleRecordRepository scheduleRecordRepository,
                                     GroupRepository groupRepository,
                                     UserRepository userRepository,
                                     LongPrimaryKeyGenerator primaryKeyGenerator,
                                     @Qualifier(SCHEDULE_RECORD_MODEL_MAPPER) OnCallModelMapper modelMapper) {
        this.scheduleRecordRepository = scheduleRecordRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
        this.modelMapper = modelMapper;
    }

    @Override
    public ScheduleRecord getScheduleRecordById(Long recordId) {
        return scheduleRecordRepository.findById(recordId).orElseThrow();
    }

    @Override
    public Collection<ScheduleRecord> getAllScheduleRecordsForUser(Long userId, LocalDateTime from, LocalDateTime to) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        return scheduleRecordRepository.findAllByUserIdAndEventDateTimeBetween(userId, from, to);
    }

    @Override
    public Collection<ScheduleRecord> getAllScheduleRecordsForGroup(Long groupId, LocalDateTime from, LocalDateTime to) {
        return scheduleRecordRepository.findAllByGroupIdAndEventDateTimeBetween(groupId, from, to);
    }

    @Override
    public ScheduleRecord createScheduleRecord(ScheduleRecord scheduleRecord) {
        if (scheduleRecord.getUser() != null && scheduleRecord.getGroup() != null) {
            throw new IllegalArgumentException();
        }
        if (scheduleRecord.getGroup() != null && !groupRepository.existsById(scheduleRecord.getGroup().getId())) {
            throw new GroupNotFoundException();
        }
        if (scheduleRecord.getUser() != null && !userRepository.existsById(scheduleRecord.getUser().getId())) {
            throw new UserNotFoundException();
        }
        scheduleRecord.setCreationDateTime(LocalDateTime.now());
        scheduleRecord.setId(primaryKeyGenerator.generatePk(ScheduleRecord.class));
        return scheduleRecordRepository.save(scheduleRecord);
    }

    @Override
    public ScheduleRecord editScheduleRecord(ScheduleRecord scheduleRecord) {
        var queriedRecord = scheduleRecordRepository.findById(scheduleRecord.getId()).orElseThrow();
        modelMapper.mapSkippingNullValues(scheduleRecord, queriedRecord);
        return scheduleRecordRepository.save(queriedRecord);
    }

    @Override
    public void deleteScheduleRecordById(Long recordId) {
        scheduleRecordRepository.deleteById(recordId);
    }
}
