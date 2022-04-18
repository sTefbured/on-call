package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.dto.ScheduleRecordDTO;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import com.stefbured.oncallserver.service.ScheduleRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/schedule")
public class ScheduleManagementController {
    private final ScheduleRecordService scheduleRecordService;

    @Autowired
    public ScheduleManagementController(ScheduleRecordService scheduleRecordService) {
        this.scheduleRecordService = scheduleRecordService;
    }

    @PostMapping()
    public ResponseEntity<ScheduleRecordDTO> addScheduleRecord(@RequestBody @Valid ScheduleRecordDTO scheduleRecord) {
        var result = scheduleRecordService.createScheduleRecord(new ScheduleRecord());
        return ResponseEntity.ok(new ScheduleRecordDTO());
    }

    @GetMapping("group/all")
    public ResponseEntity<List<ScheduleRecordDTO>> getGroupScheduleRecords(@RequestParam Long groupId,
                                                                           @RequestParam LocalDateTime from,
                                                                           @RequestParam LocalDateTime to) {
        var result = scheduleRecordService.getAllScheduleRecordsForGroup();
        return null;
    }

    @DeleteMapping
    public void deleteScheduleRecord(@RequestParam Long id) {

    }
}
