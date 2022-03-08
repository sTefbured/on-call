package com.stefbured.oncallserver.controller;

import com.stefbured.oncallserver.model.dto.validation.ViolationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice
public class ValidationErrorHandlingControllerAdvice {
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ViolationDTO>> onConstraintValidationException(ConstraintViolationException e) {
        var violations = e.getConstraintViolations().stream()
                .map(violation -> new ViolationDTO(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();
        return ResponseEntity.internalServerError().body(violations);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ViolationDTO>> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var violations = e.getFieldErrors().stream()
                .map(error -> new ViolationDTO(error.getField(), error.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(violations);
    }
}