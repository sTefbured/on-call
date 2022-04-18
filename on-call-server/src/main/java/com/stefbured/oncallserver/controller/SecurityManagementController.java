package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.exception.RedirectException;
import com.stefbured.oncallserver.model.dto.role.RoleDTO;
import com.stefbured.oncallserver.model.entity.role.Role;
import com.stefbured.oncallserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("api/v1/security")
public class SecurityManagementController {
    private final RoleService roleService;

    @Autowired
    public SecurityManagementController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("role")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDTO createRole(@Valid @RequestBody RoleDTO role) {
        var result = roleService.create(new Role());
        return new RoleDTO();
    }

    @GetMapping("role")
    @PreAuthorize("permitAll()")
    public Collection<RoleDTO> getRoles(@RequestParam int page,
                                        @RequestParam int size,
                                        HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse) {
        if (page < 1) {
            try {
                var authorizationHeader = httpServletRequest.getHeader("Authorization");
                httpServletResponse.addHeader("Authorization", authorizationHeader);
                httpServletRequest.getRequestDispatcher("role?page=1&size=" + size)
                        .forward(httpServletRequest, httpServletResponse);
                return Collections.emptyList();
            } catch (IOException | ServletException e) {
                throw new RedirectException("Exception during redirect");
            }
        }
        page--;
        var result = roleService.getRoles(page, size);
        return new ArrayList<>();
    }

    @GetMapping(value = "role", params = "id")
    @PreAuthorize("permitAll()")
    public RoleDTO getRole(@RequestParam long id) {
        var result = roleService.findById(id);
        return new RoleDTO();
    }

    @DeleteMapping("role")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@Valid @RequestBody RoleDTO roleDTO) {
        roleService.deleteById(roleDTO.getId());
    }
}
