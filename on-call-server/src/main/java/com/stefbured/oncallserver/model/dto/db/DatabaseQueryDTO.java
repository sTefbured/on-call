package com.stefbured.oncallserver.model.dto.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static com.stefbured.oncallserver.model.ModelConstants.DatabaseQuery.MAX_SQL_QUERY_LENGTH;
import static com.stefbured.oncallserver.model.ModelConstants.DatabaseQuery.SQL_QUERY_LENGTH_ERROR_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatabaseQueryDTO {
    @Size(max = MAX_SQL_QUERY_LENGTH, message = SQL_QUERY_LENGTH_ERROR_MESSAGE)
    private String query;
}
