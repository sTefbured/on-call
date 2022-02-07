package com.stefbured.oncallserver.service.user;

import com.stefbured.oncallserver.model.dto.db.DatabaseQueryResultDTO;

import java.util.Collection;
import java.util.List;

public interface DatabaseAccessService {
    List<DatabaseQueryResultDTO> runQueries(Collection<String> queries);
}
