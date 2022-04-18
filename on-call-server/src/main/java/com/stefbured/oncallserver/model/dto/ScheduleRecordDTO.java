package com.stefbured.oncallserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleRecordDTO {
    private Long id;

    private LocalDateTime eventDateTime;

    private String name;

    private String description;

    private LocalDateTime creationDateTime;

    private UserDTO user;

    private GroupDTO group;

    private UserDTO creator;
}
