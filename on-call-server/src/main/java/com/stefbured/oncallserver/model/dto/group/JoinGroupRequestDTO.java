package com.stefbured.oncallserver.model.dto.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JoinGroupRequestDTO {
    private Long id;

    private String message;

    private LocalDateTime creationDate;

    private UserDTO user;

    private GroupDTO group;
}
