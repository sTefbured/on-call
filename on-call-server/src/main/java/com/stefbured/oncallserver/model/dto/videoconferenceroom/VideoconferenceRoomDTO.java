package com.stefbured.oncallserver.model.dto.videoconferenceroom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoconferenceRoomDTO {
    private Long id;

    private String name;

    private String accessCode;

    private UserDTO creator;
}
