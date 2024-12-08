package com.radlane.payment.model.dto;

import lombok.Data;

@Data
public class ChannelPaymentResponse {
    private String paymentId;
    private String address;
    private String network;
    private String status;
    private String hostedPageUrl;
    private String errorMessage;

    public ChannelPaymentResponse(String paymentId, String address, String network, String status, String hostedPageUrl) {
        this.paymentId = paymentId;
        this.address = address;
        this.network = network;
        this.status = status;
        this.hostedPageUrl = hostedPageUrl;
    }

    public ChannelPaymentResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
