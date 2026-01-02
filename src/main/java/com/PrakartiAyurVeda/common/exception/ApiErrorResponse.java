package com.PrakartiAyurVeda.common.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
}
