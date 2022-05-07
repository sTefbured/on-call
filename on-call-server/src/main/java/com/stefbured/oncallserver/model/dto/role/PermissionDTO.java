package com.stefbured.oncallserver.model.dto.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionDTO {
    private Long id;

    private String name;

    private String description;

    private Set<RoleDTO> roles;
}
