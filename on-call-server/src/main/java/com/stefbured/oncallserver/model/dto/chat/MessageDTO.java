package com.stefbured.oncallserver.model.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDTO {
    private Long id;

    private String text;

    private LocalDateTime sendingDateTime;

    private UserDTO sender;

    private ChatDTO chat;
}
