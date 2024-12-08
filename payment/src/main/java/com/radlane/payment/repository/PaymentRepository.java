package com.radlane.payment.repository;

import com.radlane.payment.model.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Payment> findByPaymentId(String paymentId);
}