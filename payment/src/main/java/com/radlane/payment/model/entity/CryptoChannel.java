package com.radlane.payment.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;

    private String name; // Channel name
    private String description; // Channel description
    private String receiverCurrency; // Receiver currency (e.g., EUR)
    private String payCurrency; // Payment currency (e.g., BTC)
    private String address; // Crypto address
    private String network; // Blockchain network (e.g., bitcoin)
    private String projectId; // Associated project ID
    private String customId; // Custom identifier for the channel
    private String uri; // URI (e.g., bitcoin URI)
    private String hostedPageUrl; // Hosted page URL

    private String status; // Channel status (e.g., enabled)

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(insertable = false, updatable = true)
    private LocalDateTime updatedAt;

    @PreUpdate
    public void setLastUpdated() {
        this.updatedAt = LocalDateTime.now();
    }
}