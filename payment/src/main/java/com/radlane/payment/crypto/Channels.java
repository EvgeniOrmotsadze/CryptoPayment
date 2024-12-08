package com.radlane.payment.crypto;

import com.radlane.payment.model.dto.CreateChannelPaymentResponse;
import com.radlane.payment.model.dto.CreateChannelPaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class Channels {

    private final CryptoApiClient apiClient;

    public Channels(CryptoApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public CreateChannelPaymentResponse createChannel(CreateChannelPaymentRequest params) {
        return apiClient.post("/api/channels", params, CreateChannelPaymentResponse.class);
    }
}

