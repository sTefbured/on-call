package com.stefbured.oncallserver.model.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.role.UserGrantDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDTO {
    private Long id;

    private String name;

    private LocalDateTime creationDateTime;

    private UserDTO creator;

    private Set<MessageDTO> messages;

    private Set<UserGrantDTO> usersGrants;

    private GroupDTO group;
}
