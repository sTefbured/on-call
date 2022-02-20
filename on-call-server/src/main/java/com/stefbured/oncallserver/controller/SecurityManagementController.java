package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.dto.RoleDTO;
import com.stefbured.oncallserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(name = "api/v1/security")
public class SecurityManagementController {
    private final RoleService roleService;

    @Autowired
    public SecurityManagementController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("role")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDTO createRole(@RequestBody RoleDTO role) {
        return roleService.create(role);
    }

    @GetMapping(value = "role", params = {"page", "size"})
    @PreAuthorize("permitAll()")
    public Collection<RoleDTO> getRoles(@RequestParam(name = "page") int pageNumber,
                                        @RequestParam(name = "size") int pageSize,
                                        HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse) {
        if (pageNumber < 1) {
            try {
                var authorizationHeader = httpServletRequest.getHeader("Authorization");
                httpServletResponse.addHeader("Authorization", authorizationHeader);
                httpServletRequest.getRequestDispatcher("role?page=1&size=" + pageSize)
                        .forward(httpServletRequest, httpServletResponse);
                return Collections.emptyList();
            } catch (IOException | ServletException e) {
                throw new RuntimeException("Exception during redirect");
            }
        }
        pageNumber--;
        return roleService.getRoles(pageNumber, pageSize);
    }

    @GetMapping(value = "role", params = "id")
    @PreAuthorize("permitAll()")
    public RoleDTO getRole(@RequestParam(name = "id") long id) {
        return roleService.findById(id);
    }

    @DeleteMapping("role")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@RequestBody RoleDTO roleDTO) {
        roleService.deleteById(roleDTO.getId());
    }
}
