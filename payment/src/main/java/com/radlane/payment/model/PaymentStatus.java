package com.radlane.payment.model;

import lombok.Data;
import lombok.Getter;


@Getter
public enum PaymentStatus {
    PENDING("pending"),
    CREATED("created"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELLED("cancelled"),
    REFUNDED("refunded"),
    ON_HOLD("on_hold");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    // Optionally, you can create a static method to convert a string to a PaymentStatus
    public static PaymentStatus fromString(String status) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.getStatus().equalsIgnoreCase(status)) {
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
