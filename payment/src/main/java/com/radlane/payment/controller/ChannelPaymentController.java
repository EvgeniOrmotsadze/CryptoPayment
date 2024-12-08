package com.radlane.payment.controller;

import com.radlane.payment.model.dto.CreateChannelPaymentRequest;
import com.radlane.payment.model.dto.ChannelPaymentResponse;
import com.radlane.payment.service.ChannelPaymentService;
import com.radlane.payment.service.CryptoCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Slf4j
public class ChannelPaymentController {

    private final ChannelPaymentService channelPaymentService;
    private final CryptoCallbackService cryptoCallbackService;

    @PostMapping()
    public ResponseEntity<ChannelPaymentResponse> createChannelPayment(@RequestBody CreateChannelPaymentRequest request) {
        return ResponseEntity.ok(channelPaymentService.createChannelPayment(request));
    }

    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody String body,
                                                 @RequestHeader("X-Cryptopay-Signature") String signature) {
        log.info("Received callback: {}", body);
        if (!cryptoCallbackService.verifySignature(body, signature)) {
            log.warn("Invalid callback signature");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        cryptoCallbackService.processCallback(body);
        return ResponseEntity.ok().build();
    }
}
