package com.stefbured.oncallserver.model.dto.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.chat.ChatDTO;
import com.stefbured.oncallserver.model.dto.group.GroupDTO;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGrantDTO {
    private Long id;

    private UserDTO user;

    private RoleDTO role;

    private GroupDTO group;

    private ChatDTO chat;
}
