package com.stefbured.oncallserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import com.stefbured.oncallserver.model.entity.user.rights.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGroupDTO {
    private Long id;

    private String idTag;

    private String name;

    private String description;

    private Timestamp creationDate;

    private UserDTO creator;

    private UserDTO owner;

    private UserGroupDTO parentGroup;

    private Set<UserDTO> members;

    private Set<Permission> permissions;

    private Set<RoleDTO> roles;
}
