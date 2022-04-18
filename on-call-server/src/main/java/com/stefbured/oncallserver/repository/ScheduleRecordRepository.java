package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRecordRepository extends JpaRepository<ScheduleRecord, Long> {

}
