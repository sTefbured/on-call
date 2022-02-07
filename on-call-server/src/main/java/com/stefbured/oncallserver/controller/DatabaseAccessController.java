package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.dto.db.DatabaseQueryResultDTO;
import com.stefbured.oncallserver.service.user.DatabaseAccessService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/db")
public class DatabaseAccessController {
    private final DatabaseAccessService databaseAccessService;

    @Autowired
    public DatabaseAccessController(DatabaseAccessService databaseAccessService) {
        this.databaseAccessService = databaseAccessService;
    }

    @PostMapping()
    public List<DatabaseQueryResultDTO> runQuery(@RequestBody QueryHandler queryHandler,
                                                 HttpServletResponse httpServletResponse) throws IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var isNotGranted = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(authority -> authority.equals("database:read") || authority.equals("database:write"));
        if (isNotGranted) {
            httpServletResponse.sendRedirect("/404");
            return Collections.emptyList();
        }
        var queryText = queryHandler.getQueryText().trim();
        var queries = Arrays.stream(queryText.split(";")).toList();
        return databaseAccessService.runQueries(queries);
    }

    @Data
    static class QueryHandler {
        private String queryText;
    }
}
