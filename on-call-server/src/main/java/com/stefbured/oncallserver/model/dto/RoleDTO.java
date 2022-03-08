package com.stefbured.oncallserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.user.rights.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.Role.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {
    private Long id;

    @Size(max = MAX_ROLE_NAME_LENGTH, message = ROLE_NAME_LENGTH_ERROR_MESSAGE)
    private String name;

    @Size(max = MAX_ROLE_DESCRIPTION_LENGTH, message = ROLE_DESCRIPTION_LENGTH_ERROR_MESSAGE)
    private String description;

    private Set<User> users;

    private Set<Permission> permissions;
}
