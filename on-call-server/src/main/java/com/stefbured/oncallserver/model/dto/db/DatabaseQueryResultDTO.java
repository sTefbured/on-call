package com.stefbured.oncallserver.model.dto.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class DatabaseQueryResultDTO {
    private String query;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> columnNames;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<List<String>> dataRows;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exceptionMessage;
}
