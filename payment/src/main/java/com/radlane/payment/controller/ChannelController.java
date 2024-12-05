package com.radlane.payment.controller;

import com.radlane.payment.model.entity.CryptoChannel;
import com.radlane.payment.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<CryptoChannel> createChannel(@RequestParam Long customerId, @RequestParam String cryptoType) {
        com.radlane.payment.model.entity.CryptoChannel cryptoChannel = channelService.createChannel(customerId, cryptoType);
        return ResponseEntity.ok(cryptoChannel);
    }
}