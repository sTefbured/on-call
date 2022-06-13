package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.GroupNotFoundException;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import com.stefbured.oncallserver.repository.GroupRepository;
import com.stefbured.oncallserver.repository.ScheduleRecordRepository;
import com.stefbured.oncallserver.repository.UserRepository;
import com.stefbured.oncallserver.service.NotificationService;
import com.stefbured.oncallserver.service.ScheduleRecordService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

import static com.stefbured.oncallserver.OnCallConstants.NotificationTypes.SCHEDULED_EVENT;
import static com.stefbured.oncallserver.mapper.ScheduleRecordModelMapper.SCHEDULE_RECORD_MODEL_MAPPER;

@Service
public class ScheduleRecordServiceImpl implements ScheduleRecordService {
    private static final Logger LOGGER = LogManager.getLogger(ScheduleRecordServiceImpl.class);

    private static final long SIXTEEN_MINUTES_IN_SECONDS = 16 * 60L;
    private static final long FIFTEEN_MINUTES_IN_SECONDS = 15 * 60L;

    private static final long ELEVEN_MINUTES_IN_SECONDS = 11 * 60L;
    private static final long TEN_MINUTES_IN_SECONDS = 10 * 60L;

    private static final long SIX_MINUTES_IN_SECONDS = 6 * 60L;
    private static final long FIVE_MINUTES_IN_SECONDS = 5 * 60L;

    private static final long THREE_MINUTES_IN_SECONDS = 3 * 60L;
    private static final long TWO_MINUTES_IN_SECONDS = 2 * 60L;

    private static final long ONE_MINUTE_IN_SECONDS = 60L;
    private static final long ZERO_MINUTES_IN_SECONDS = 0L;

    private final ScheduleRecordRepository scheduleRecordRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;
    private final OnCallModelMapper modelMapper;
    private final NotificationService notificationService;

    @Autowired
    public ScheduleRecordServiceImpl(ScheduleRecordRepository scheduleRecordRepository,
                                     GroupRepository groupRepository,
                                     UserRepository userRepository,
                                     LongPrimaryKeyGenerator primaryKeyGenerator,
                                     @Qualifier(SCHEDULE_RECORD_MODEL_MAPPER) OnCallModelMapper modelMapper,
                                     NotificationService notificationService) {
        this.scheduleRecordRepository = scheduleRecordRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
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

    @Override
    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void sendNotificationsForActiveEvents() {
        LOGGER.debug("Searching for events to be notified about");
        var currentTime = LocalDateTime.now().withNano(0).withSecond(0);
        var from = currentTime.minusMinutes(1L).withSecond(1);
        var to = currentTime.plusMinutes(15).withSecond(59).withNano(0);
        var scheduleRecords = scheduleRecordRepository.findAllByEventDateTimeBetween(from, to);
        var currentTimeSeconds = currentTime.toEpochSecond(ZoneOffset.UTC);
        LOGGER.debug("Found {} events between {} and {}", scheduleRecords.size(), from, to);
        scheduleRecords.forEach(scheduleRecord -> {
            var eventStart = scheduleRecord.getEventDateTime().toEpochSecond(ZoneOffset.UTC);
            var timeLeft = eventStart - currentTimeSeconds;
            if (isBetween(timeLeft, -ONE_MINUTE_IN_SECONDS + 1, ZERO_MINUTES_IN_SECONDS)) {
                var notificationMessage = "Event '" + scheduleRecord.getName() + "' has started.";
                LOGGER.debug(notificationMessage);
                notificationService.createNotification(scheduleRecord.getCreator().getId(), scheduleRecord.getId(),
                        notificationMessage, SCHEDULED_EVENT, scheduleRecord.getUser().getId());
            } else if (isBetween(timeLeft, FIFTEEN_MINUTES_IN_SECONDS, SIXTEEN_MINUTES_IN_SECONDS - 1)
                    || isBetween(timeLeft, TEN_MINUTES_IN_SECONDS, ELEVEN_MINUTES_IN_SECONDS - 1)
                    || isBetween(timeLeft, FIVE_MINUTES_IN_SECONDS, SIX_MINUTES_IN_SECONDS - 1)
                    || isBetween(timeLeft, TWO_MINUTES_IN_SECONDS, THREE_MINUTES_IN_SECONDS - 1)) {
                var notificationMessage = "Event '" + scheduleRecord.getName() + "' starts in "
                        + (timeLeft / ONE_MINUTE_IN_SECONDS) + " minutes";
                LOGGER.debug(notificationMessage);
                notificationService.createNotification(scheduleRecord.getCreator().getId(), scheduleRecord.getId(),
                        notificationMessage, SCHEDULED_EVENT, scheduleRecord.getUser().getId());
            }
        });
    }

    private boolean isBetween(long value, long from, long to) {
        return value >= from && value <= to;
    }
}
