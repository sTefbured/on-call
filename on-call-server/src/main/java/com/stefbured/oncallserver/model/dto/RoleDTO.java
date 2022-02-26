package com.stefbured.oncallserver.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stefbured.oncallserver.model.entity.user.User;
import com.stefbured.oncallserver.model.entity.user.rights.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private Set<User> users;
    private Set<Permission> permissions;
}
