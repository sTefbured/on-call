package com.stefbured.oncallserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.rights.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Set;

import static com.stefbured.oncallserver.model.ModelConstants.UserGroup.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGroupDTO {
    private Long id;

    @Size(max = MAX_USER_GROUP_ID_TAG_LENGTH, message = USER_GROUP_ID_TAG_LENGTH_ERROR_MESSAGE)
    private String idTag;

    @Size(min = MIN_USER_GROUP_NAME_LENGTH, max = MAX_USER_GROUP_NAME_LENGTH, message = USER_GROUP_NAME_LENGTH_ERROR_MESSAGE)
    private String name;

    @Size(max = MAX_USER_GROUP_DESCRIPTION_LENGTH, message = USER_GROUP_DESCRIPTION_LENGTH_ERROR_MESSAGE)
    private String description;

    @PastOrPresent(message = CREATION_DATE_VALIDATION_ERROR_MESSAGE)
    private Timestamp creationDate;

    private UserDTO creator;

    private UserDTO owner;

    private UserGroupDTO parentGroup;

    private Set<UserDTO> members;

    private Set<Permission> permissions;

    private Set<RoleDTO> roles;
}
