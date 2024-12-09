package com.radlane.payment.exception;


import com.radlane.payment.model.dto.GeneralErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomPaymentException.class)
    public ResponseEntity<GeneralErrorResponse> handleCustomPaymentException(CustomPaymentException ex, WebRequest request) {
        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                ex.getErrorCode().toString(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(generalErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        GeneralErrorResponse generalErrorResponse = new GeneralErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                request.getDescription(false)
        );
        return new ResponseEntity<>(generalErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
