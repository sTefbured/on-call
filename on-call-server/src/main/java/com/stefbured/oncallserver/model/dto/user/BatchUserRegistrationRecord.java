package com.stefbured.oncallserver.model.dto.user;

import lombok.Data;

@Data
public class BatchUserRegistrationRecord {
    private UserRegisterDTO user;
    private boolean isSuccessful;
    private String exceptionMessage;
}
