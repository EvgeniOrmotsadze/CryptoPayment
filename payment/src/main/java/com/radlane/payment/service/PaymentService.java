package com.radlane.payment.service;

import com.radlane.payment.model.entity.Payment;
import com.radlane.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment createPayment(Payment payment) {
        if (paymentRepository.findByTransactionId(payment.getTransactionId()).isPresent()) {
            throw new IllegalArgumentException("Duplicate transaction ID");
        }
        payment.setStatus("PENDING");
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }
}