package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.config.OnCallPermissionEvaluator;
import com.stefbured.oncallserver.mapper.OnCallModelMapper;
import com.stefbured.oncallserver.model.dto.ScheduleRecordDTO;
import com.stefbured.oncallserver.model.entity.schedule.ScheduleRecord;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.service.ScheduleRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

import static com.stefbured.oncallserver.OnCallConstants.*;
import static com.stefbured.oncallserver.mapper.ScheduleRecordModelMapper.*;

@RestController
@RequestMapping("api/v1/schedule")
public class ScheduleRecordController {
    private final ScheduleRecordService scheduleRecordService;
    private final OnCallModelMapper scheduleRecordMapper;

    @Autowired
    public ScheduleRecordController(ScheduleRecordService scheduleRecordService,
                                    @Qualifier(SCHEDULE_RECORD_MODEL_MAPPER) OnCallModelMapper scheduleRecordMapper) {
        this.scheduleRecordService = scheduleRecordService;
        this.scheduleRecordMapper = scheduleRecordMapper;
    }

    @PostMapping
    @PreAuthorize("authentication.details.equals(#scheduleRecord?.user?.id) " +
            "|| hasPermission(#scheduleRecord?.group?.id, 'group', '" + SCHEDULE_RECORD_CREATE + "') " +
            "|| hasPermission(null, 'global', '" + SCHEDULE_RECORD_CREATE + "')")
    public ResponseEntity<ScheduleRecordDTO> addScheduleRecord(@RequestBody @Valid ScheduleRecordDTO scheduleRecord,
                                                               HttpServletRequest request) {
        var recordEntity = new ScheduleRecord();
        scheduleRecordMapper.mapSkippingNullValues(scheduleRecord, recordEntity);
        var creator = new User();
        creator.setId((Long) SecurityContextHolder.getContext().getAuthentication().getDetails());
        recordEntity.setCreator(creator);
        var createdRecord = scheduleRecordService.createScheduleRecord(recordEntity);
        var result = scheduleRecordMapper.map(createdRecord, ScheduleRecordDTO.class, SCHEDULE_RECORD_TO_FULL_DTO);
        var locationUri = URI.create(request.getRequestURI()).resolve(result.getId().toString());
        return ResponseEntity.created(locationUri).body(result);
    }

    @GetMapping("{recordId}")
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasPermission(null, 'global', '" + SCHEDULE_RECORD_VIEW + "') " +
            "|| (hasPermission(returnObject.body?.group?.id, 'group', '" + SCHEDULE_RECORD_VIEW + "')) " +
            "|| (authentication.details.equals(returnObject.body?.user?.id))")
    public ResponseEntity<ScheduleRecordDTO> getScheduleRecord(@PathVariable Long recordId) {
        var queriedRecord = scheduleRecordService.getScheduleRecordById(recordId);
        var result = scheduleRecordMapper.map(queriedRecord, ScheduleRecordDTO.class, SCHEDULE_RECORD_TO_FULL_DTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("group/{groupId}")
    @PreAuthorize("hasPermission(#groupId, 'group', '" + SCHEDULE_RECORD_VIEW + "') " +
            "|| hasPermission(null, 'global', '" + SCHEDULE_RECORD_VIEW + "')")
    public ResponseEntity<Iterable<ScheduleRecordDTO>> getGroupScheduleRecords(@PathVariable Long groupId,
                                                                               @RequestParam LocalDateTime from,
                                                                               @RequestParam LocalDateTime to) {
        var queriedRecords = scheduleRecordService.getAllScheduleRecordsForGroup(groupId, from, to);
        var result = scheduleRecordMapper.mapCollection(queriedRecords, ScheduleRecordDTO.class, SCHEDULE_RECORD_TO_PREVIEW_DTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("user/{userId}")
    @PreAuthorize("#userId.equals(authentication.details) || hasPermission(null, 'global', '" + SCHEDULE_RECORD_VIEW + "')")
    public ResponseEntity<Iterable<ScheduleRecordDTO>> getUserScheduleRecords(@PathVariable Long userId,
                                                                              @RequestParam LocalDateTime from,
                                                                              @RequestParam LocalDateTime to) {
        var queriedRecords = scheduleRecordService.getAllScheduleRecordsForUser(userId, from, to);
        var result = scheduleRecordMapper.mapCollection(queriedRecords, ScheduleRecordDTO.class, SCHEDULE_RECORD_TO_PREVIEW_DTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    @PreAuthorize("#scheduleRecord.creator.id.equals(authentication.details) " +
            "|| hasPermission(null, 'global', '" + SCHEDULE_RECORD_EDIT + "')")
    public ResponseEntity<Object> editScheduleRecord(@RequestBody ScheduleRecordDTO scheduleRecord) {
        var queriedRecord = scheduleRecordService.getScheduleRecordById(scheduleRecord.getId());
        if (!queriedRecord.getCreator().getId().equals(scheduleRecord.getCreator().getId())) {
            return ResponseEntity.badRequest().body("Wrong creator id set to the schedule record");
        }
        var recordEntity = new ScheduleRecord();
        scheduleRecord.setUser(null);
        scheduleRecord.setGroup(null);
        scheduleRecord.setCreator(null);
        scheduleRecordMapper.mapSkippingNullValues(scheduleRecord, recordEntity);
        var updatedRecord = scheduleRecordService.editScheduleRecord(recordEntity);
        var result = scheduleRecordMapper.map(updatedRecord, ScheduleRecordDTO.class, SCHEDULE_RECORD_TO_FULL_DTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{recordId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteScheduleRecord(@PathVariable Long recordId) {
        var queriedRecord = scheduleRecordService.getScheduleRecordById(recordId);
        if (!isPermittedToDeleteRecord(queriedRecord)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        scheduleRecordService.deleteScheduleRecordById(recordId);
        return ResponseEntity.ok("Schedule record deleted");
    }

    private boolean isPermittedToDeleteRecord(ScheduleRecord scheduleRecord) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getDetails();
        var permissionEvaluator = OnCallPermissionEvaluator.getInstance();
        var groupId = scheduleRecord.getGroup() != null ? scheduleRecord.getGroup().getId() : null;
        return userId.equals(scheduleRecord.getCreator().getId())
                || permissionEvaluator.hasPermission("global", SCHEDULE_RECORD_DELETE)
                || permissionEvaluator.hasPermission(groupId, "group", SCHEDULE_RECORD_DELETE);
    }
}
