package com.stefbured.oncallserver.model.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchUserRegistrationRecordDTO {
    private UserDTO user;
    private boolean isSuccessful;
    private String exceptionMessage;
}
