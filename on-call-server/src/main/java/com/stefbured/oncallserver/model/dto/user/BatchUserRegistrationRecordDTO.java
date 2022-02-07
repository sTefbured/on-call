package com.stefbured.oncallserver.model.dto.user;

import lombok.Data;

@Data
public class BatchUserRegistrationRecordDTO {
    private UserRegisterDTO user;
    private boolean isSuccessful;
    private String exceptionMessage;
}
