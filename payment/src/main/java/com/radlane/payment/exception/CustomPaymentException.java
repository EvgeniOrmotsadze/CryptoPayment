package com.radlane.payment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomPaymentException extends RuntimeException {
    private final HttpStatus errorCode;

    public CustomPaymentException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
