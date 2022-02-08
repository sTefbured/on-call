package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.dto.db.DatabaseQueryResultDTO;
import com.stefbured.oncallserver.service.user.DatabaseAccessService;
import com.stefbured.oncallserver.utils.SqlScriptSplitter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<DatabaseQueryResultDTO>> runQuery(@RequestBody QueryHandler queryHandler) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var isNotGranted = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(authority -> authority.equals("database:read") || authority.equals("database:write"));
        if (isNotGranted) {
            return ResponseEntity.notFound().build();
        }
        var queryText = queryHandler.getQueryText().trim();
        var queries = new SqlScriptSplitter().split(queryText);
        return ResponseEntity.ok(databaseAccessService.runQueries(queries));
    }

    @Data
    static class QueryHandler {
        private String queryText;
    }
}
