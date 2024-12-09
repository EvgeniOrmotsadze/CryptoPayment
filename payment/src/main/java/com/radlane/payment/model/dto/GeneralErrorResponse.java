package com.radlane.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GeneralErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String errorCode;
    private String path;
}