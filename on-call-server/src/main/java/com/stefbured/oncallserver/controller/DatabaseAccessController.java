package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.dto.db.DatabaseQueryDTO;
import com.stefbured.oncallserver.model.dto.db.DatabaseQueryResultDTO;
import com.stefbured.oncallserver.service.DatabaseAccessService;
import com.stefbured.oncallserver.utils.SqlScriptSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.stefbured.oncallserver.OnCallConstants.DATABASE_ACCESS;
import static com.stefbured.oncallserver.config.OnCallPermissionEvaluator.GLOBAL_TARGET_TYPE;

@RestController
@RequestMapping("api/v1/db")
public class DatabaseAccessController {
    private final DatabaseAccessService databaseAccessService;

    @Autowired
    public DatabaseAccessController(DatabaseAccessService databaseAccessService) {
        this.databaseAccessService = databaseAccessService;
    }

    @PostMapping
    @PreAuthorize("hasPermission(null, '" + GLOBAL_TARGET_TYPE + "', '" + DATABASE_ACCESS + "')")
    public ResponseEntity<List<DatabaseQueryResultDTO>> runQuery(@Valid @RequestBody DatabaseQueryDTO databaseQuery) {
        var queryText = databaseQuery.getQuery().trim();
        var queries = new SqlScriptSplitter().split(queryText);
        return ResponseEntity.ok(databaseAccessService.runQueries(queries));
    }
}
