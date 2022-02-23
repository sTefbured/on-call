package com.stefbured.oncallserver.model.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDateTime birthDate;

    private LocalDateTime registrationDate;

    private LocalDateTime lastVisitDate;

    private LocalDateTime passwordExpirationDate;

    private LocalDateTime userExpirationDate;

    private Boolean isBanned;

    private Boolean isEnabled;
}
