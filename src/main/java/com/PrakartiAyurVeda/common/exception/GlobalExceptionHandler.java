package com.PrakartiAyurVeda.common.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 🔹 Validation errors (@Valid annotation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);
        response.put("errorCode", "VALIDATION_FAILED");
        response.put("timestamp", LocalDateTime.now());

        log.warn("Validation failed: {}", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // 🔹 Illegal arguments / validation issues
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex) {

        log.warn("Client error: {}", ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(new ApiErrorResponse(
                        ex.getMessage(),
                        "BAD_REQUEST",
                        LocalDateTime.now()
                ));
    }

    // 🔹 Entity not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            EntityNotFoundException ex) {

        log.warn("Entity not found: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(
                        ex.getMessage(),
                        "NOT_FOUND",
                        LocalDateTime.now()
                ));
    }

    // 🔹 Fallback (unexpected errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(
            Exception ex) {

        log.error("Unexpected error", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(
                        "Something went wrong. Please try again later.",
                        "INTERNAL_ERROR",
                        LocalDateTime.now()
                ));
    }
}
