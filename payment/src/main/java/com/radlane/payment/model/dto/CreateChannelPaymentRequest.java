package com.radlane.payment.model.dto;

import lombok.Data;

@Data
public class CreateChannelPaymentRequest {
    private String pay_currency;
    private String network;
    private String receiver_currency;
    private String name;
    private String custom_id;
}
