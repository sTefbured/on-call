package com.stefbured.oncallserver.model.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.RoleDTO;
import com.stefbured.oncallserver.model.dto.UserGroupDTO;
import lombok.*;

import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.User.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;

    @Size(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH, message = USERNAME_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = USERNAME_VALIDATION_REGEX, message = USERNAME_VALIDATION_ERROR_MESSAGE)
    private String username;

    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = PASSWORD_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = PASSWORD_VALIDATION_REGEX, message = PASSWORD_VALIDATION_ERROR_MESSAGE)
    private String password;

    @Size(max = MAX_EMAIL_LENGTH, message = EMAIL_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = EMAIL_VALIDATION_REGEX, message = EMAIL_VALIDATION_ERROR_MESSAGE)
    private String email;

    @Size(max = MAX_NAME_LENGTH, message = FIRST_NAME_LENGTH_ERROR_MESSAGE)
    private String firstName;

    @Size(max = MAX_NAME_LENGTH, message = LAST_NAME_LENGTH_ERROR_MESSAGE)
    private String lastName;

    @Past(message = BIRTH_DATE_IN_FUTURE_ERROR_MESSAGE)
    private LocalDate birthDate;

    @PastOrPresent(message = REGISTRATION_DATE_VALIDATION_ERROR_MESSAGE)
    private LocalDateTime registrationDate;

    @PastOrPresent(message = LAST_VISIT_DATE_VALIDATION_ERROR_MESSAGE)
    private LocalDateTime lastVisitDate;

    private LocalDateTime passwordExpirationDate;

    private LocalDateTime userExpirationDate;

    private Boolean isBanned;

    private Boolean isEnabled;

    private Set<RoleDTO> roles;

    private Set<UserGroupDTO> userGroups;
}
