package com.stefbured.oncallserver.model.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.RoleDTO;
import com.stefbured.oncallserver.model.dto.UserGroupDTO;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
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

    private Set<RoleDTO> roles;

    private Set<UserGroupDTO> userGroups;
}
