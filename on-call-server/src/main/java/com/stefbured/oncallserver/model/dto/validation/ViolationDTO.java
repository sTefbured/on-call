package com.stefbured.oncallserver.model.dto.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViolationDTO {
    private String fieldName;
    private String message;
}
