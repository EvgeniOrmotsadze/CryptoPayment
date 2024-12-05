package com.radlane.payment.repository;


import com.radlane.payment.model.entity.CryptoChannel;
import com.radlane.payment.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<CryptoChannel, Long> {
    Optional<CryptoChannel> findByCustomerAndCryptoType(Customer customer, String cryptoType);
}