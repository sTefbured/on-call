package com.stefbured.oncallserver.model.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime birthDate;
}
