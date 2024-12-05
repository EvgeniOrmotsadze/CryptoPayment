package com.radlane.payment.service;

import com.radlane.payment.model.entity.CryptoChannel;
import com.radlane.payment.model.entity.Customer;
import com.radlane.payment.repository.ChannelRepository;
import com.radlane.payment.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final CustomerRepository customerRepository;

    public CryptoChannel createChannel(Long customerId, String cryptoType) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        return channelRepository.findByCustomerAndCryptoType(customer, cryptoType)
                .orElseGet(() -> {
                    CryptoChannel channel = new CryptoChannel();
                    channel.setCustomer(customer);
                    channel.setCryptoType(cryptoType);
                    channel.setAddress(generateCryptoAddress(cryptoType)); // Replace with actual API call
                    return channelRepository.save(channel);
                });
    }

    private String generateCryptoAddress(String cryptoType) {
        // Simulate API call to Cryptopay
        return cryptoType + "_ADDR_" + UUID.randomUUID();
    }
}
