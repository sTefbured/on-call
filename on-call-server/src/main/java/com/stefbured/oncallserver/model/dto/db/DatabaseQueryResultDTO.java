package com.stefbured.oncallserver.model.dto.db;

import lombok.Data;

import java.util.List;

@Data
public class DatabaseQueryResultDTO {
    private String query;
    private List<String> columnNames;
    private List<List<String>> dataRows;
    private String exceptionMessage;
}
