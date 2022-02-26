package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.dto.db.DatabaseQueryDTO;
import com.stefbured.oncallserver.model.dto.db.DatabaseQueryResultDTO;
import com.stefbured.oncallserver.service.DatabaseAccessService;
import com.stefbured.oncallserver.utils.SqlScriptSplitter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/v1/db")
public class DatabaseAccessController {
    private final DatabaseAccessService databaseAccessService;

    @Autowired
    public DatabaseAccessController(DatabaseAccessService databaseAccessService) {
        this.databaseAccessService = databaseAccessService;
    }

    @PostMapping
    public ResponseEntity<List<DatabaseQueryResultDTO>> runQuery(@RequestBody DatabaseQueryDTO databaseQuery,
                                                                 HttpServletRequest request) {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            return ResponseEntity.notFound().build();
        }
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var isNotGranted = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(authority -> authority.equals("database:runQuery"));
        if (isNotGranted) {
            return ResponseEntity.notFound().build();
        }
        var queryText = databaseQuery.getQuery().trim();
        var queries = new SqlScriptSplitter().split(queryText);
        return ResponseEntity.ok(databaseAccessService.runQueries(queries));
    }
}
