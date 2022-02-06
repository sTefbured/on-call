package com.stefbured.oncallserver.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime birthDate;
    private LocalDateTime lastVisitDate;
}
