package com.stefbured.oncallserver.model.dto.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.Role.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {
    private Long id;

    @Size(max = MAX_ROLE_NAME_LENGTH, message = ROLE_NAME_LENGTH_ERROR_MESSAGE)
    private String name;

    @Size(max = MAX_ROLE_DESCRIPTION_LENGTH, message = ROLE_DESCRIPTION_LENGTH_ERROR_MESSAGE)
    private String description;

    private RoleTypeDTO roleType;

    private Set<UserGrantDTO> userGrants;

    private Set<PermissionDTO> permissions;
}
