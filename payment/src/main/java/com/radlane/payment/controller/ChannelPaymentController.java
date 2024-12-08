package com.radlane.payment.controller;

import com.radlane.payment.model.dto.ChannelPaymentResponse;
import com.radlane.payment.model.dto.CreateChannelPaymentRequest;
import com.radlane.payment.service.ChannelPaymentService;
import com.radlane.payment.service.CryptoCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Channel Payment API", description = "API for managing channel payments and handling callbacks")
public class ChannelPaymentController {

    private final ChannelPaymentService channelPaymentService;
    private final CryptoCallbackService cryptoCallbackService;


    @Operation(summary = "Create a new channel payment",
            description = "Create a channel payment with the provided details.",
            tags = {"Channel Payment API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channel payment created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChannelPaymentResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content)
    })
    @PostMapping()
    public ResponseEntity<ChannelPaymentResponse> createChannelPayment(@RequestBody CreateChannelPaymentRequest request) {
        return ResponseEntity.ok(channelPaymentService.createChannelPayment(request));
    }


    @Operation(summary = "Handle callback from Cryptopay",
            description = "Process a callback from Cryptopay and verify its signature.",
            tags = {"Channel Payment API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Callback processed successfully",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Invalid signature",
                    content = @Content)
    })
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

    @GetMapping("{channel-id}/payments/{channel-payment-id}")
    public ResponseEntity<String> getChannelPayments(@PathVariable("channel-id") String channelId,
                                                     @PathVariable("channel-payment-id") String channelPaymentId){
        //todo
        return ResponseEntity.ok().build();
    }
}
