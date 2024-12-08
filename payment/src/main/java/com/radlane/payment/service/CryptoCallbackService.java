package com.radlane.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radlane.payment.crypto.util.CallbackVerifier;
import com.radlane.payment.model.PaymentStatus;
import com.radlane.payment.model.dto.ChannelPaymentCallbackRequest;
import com.radlane.payment.model.entity.Payment;
import com.radlane.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class CryptoCallbackService {

    private final CallbackVerifier callbackVerifier;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;

    public CryptoCallbackService(CallbackVerifier callbackVerifier, ObjectMapper objectMapper, PaymentRepository paymentRepository) {
        this.callbackVerifier = callbackVerifier;
        this.objectMapper = objectMapper;
        this.paymentRepository = paymentRepository;
    }

    // Verify signature to ensure the callback is authentic
    public boolean verifySignature(String body, String signature) {
        return callbackVerifier.verify(body, signature);
    }

    // Process the callback payload
    @Transactional
    public void processCallback(String body) {
        try {
            ChannelPaymentCallbackRequest channelPaymentCallbackRequest = objectMapper.readValue(body, ChannelPaymentCallbackRequest.class);
            ChannelPaymentCallbackRequest.CallbackData data = channelPaymentCallbackRequest.getData();

            String paymentId = data.getId();
            String status = data.getStatus();
            String statusContext = data.getStatusContext();

            // Check if the payment already exists in the database or create a new payment if not found
            Payment payment = paymentRepository.findByPaymentId(paymentId)
                    .orElseGet(() -> createPaymentFromCallback(data));

            // Idempotency: Skip processing if the status is already completed or refunded
            if (payment.getStatus() == PaymentStatus.COMPLETED ||
                    payment.getStatus() == PaymentStatus.REFUNDED ||
                    payment.getStatus() == PaymentStatus.CANCELLED) {
                log.warn("Payment already processed for paymentId: {}", payment.getPaymentId());
                return;
            }

            // Business Logic for Different Statuses
            switch (status) {
                case "pending" -> handlePending(payment);
                case "completed" -> handleCompleted(payment, data);
                case "on_hold" -> handleOnHold(payment, statusContext);
                case "refunded" -> handleRefunded(payment);
                case "cancelled" -> handleCancelled(payment);
                default -> log.warn("Unhandled status: {}", status);
            }

        } catch (Exception e) {
            log.error("Error processing callback", e);
        }
    }

    // Handle pending status
    private void handlePending(Payment payment) {
        log.info("Handling pending status for paymentId: {}", payment.getPaymentId());
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
    }

    // Handle completed status
    private void handleCompleted(Payment payment, ChannelPaymentCallbackRequest.CallbackData data) {
        log.info("Handling completed status for paymentId: {}", payment.getPaymentId());

        BigDecimal paidAmount = new BigDecimal(data.getPaidAmount());
        String paidCurrency = data.getPaidCurrency();
        BigDecimal receivedAmount = new BigDecimal(data.getReceivedAmount());
        String receivedCurrency = data.getReceivedCurrency();
        BigDecimal fee = new BigDecimal(data.getFee());
        String feeCurrency = data.getFeeCurrency();

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAmount(paidAmount);
        payment.setPaidCurrency(paidCurrency);
        payment.setReceivedAmount(receivedAmount);
        payment.setReceivedCurrency(receivedCurrency);
        payment.setFee(fee);
        payment.setFeeCurrency(feeCurrency);

        paymentRepository.save(payment);

        log.info("Payment completed for paymentId: {}", payment.getPaymentId());
    }

    // Handle on_hold status
    private void handleOnHold(Payment payment, String statusContext) {
        log.info("Handling on_hold status for paymentId: {}", payment.getPaymentId());
        if ("illicit_resource".equals(statusContext)) {
            log.warn("High-risk transaction detected. Taking action for paymentId: {}", payment.getPaymentId());
        } else if ("channel_disabled".equals(statusContext)) {
            log.warn("Payment on hold due to disabled channel for paymentId: {}", payment.getPaymentId());
        }
    }

    // Handle refunded status
    private void handleRefunded(Payment payment) {
        log.info("Handling refunded status for paymentId: {}", payment.getPaymentId());
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
    }

    // Handle cancelled status
    private void handleCancelled(Payment payment) {
        log.info("Handling cancelled status for paymentId: {}", payment.getPaymentId());
        payment.setStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
    }

    // Utility method to create a Payment entity from the callback data if it does not exist
    private Payment createPaymentFromCallback(ChannelPaymentCallbackRequest.CallbackData data) {
        Payment payment = new Payment();
        payment.setPaymentId(data.getId());
        payment.setAddress(data.getAddress());
        payment.setNetwork(data.getNetwork());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(data.getCreatedAt());
        paymentRepository.save(payment);
        log.info("New payment created for paymentId: {}", payment.getPaymentId());
        return payment;
    }
}

