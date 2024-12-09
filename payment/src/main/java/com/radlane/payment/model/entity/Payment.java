package com.radlane.payment.model.entity;

import com.radlane.payment.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version  // Optimistic locking: Version field ensures no concurrent updates
    private Long version;

    @Column(unique = true, nullable = false)
    private String paymentId;
    @Column(name = "channel_id")
    private Long channelId;
    private String customerId;
    private String address;
    private String network;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private BigDecimal paidAmount;
    private String paidCurrency;
    private BigDecimal receivedAmount;
    private String receivedCurrency;
    private BigDecimal fee;
    private String feeCurrency;
    private String createdAt;
}
