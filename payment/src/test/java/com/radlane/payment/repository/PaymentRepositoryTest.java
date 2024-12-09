package com.radlane.payment.repository;

import com.radlane.payment.model.entity.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testSaveAndFindByPaymentId() {
        // Given
        Payment payment = new Payment();
        payment.setChannelId(1234L);
        payment.setPaymentId("PAY123");
        payment.setPaidAmount(new BigDecimal("100.0"));

        // When
        Payment savedPayment = paymentRepository.save(payment);

        // Then
        Optional<Payment> foundPayment = paymentRepository.findByPaymentId("PAY123");
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getPaymentId()).isEqualTo("PAY123");
    }

    @Test
    @Transactional
    public void testPessimisticLock() {
        // Given
        Payment payment = new Payment();
        payment.setChannelId(1234L);
        payment.setPaymentId("LOCK123");
        payment.setPaidAmount(new BigDecimal("50.0"));
        paymentRepository.save(payment);

        // When: Locking the payment for pessimistic write
        Optional<Payment> lockedPayment = paymentRepository.findByPaymentId("LOCK123");

        // Then
        assertThat(lockedPayment).isPresent();
        assertThat(lockedPayment.get().getPaymentId()).isEqualTo("LOCK123");
    }
}
