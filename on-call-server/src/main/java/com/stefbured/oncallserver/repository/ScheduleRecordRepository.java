package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface ScheduleRecordRepository extends JpaRepository<ScheduleRecord, Long> {
    Collection<ScheduleRecord> findAllByUserIdAndEventDateTimeBetween(Long userId, LocalDateTime from, LocalDateTime to);
    Collection<ScheduleRecord> findAllByGroupIdAndEventDateTimeBetween(Long groupId, LocalDateTime from, LocalDateTime to);
}
