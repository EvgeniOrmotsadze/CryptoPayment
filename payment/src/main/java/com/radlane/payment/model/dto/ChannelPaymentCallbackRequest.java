package com.radlane.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChannelPaymentCallbackRequest {

    @JsonProperty("type")
    private String type;

    @JsonProperty("event")
    private String event;

    @JsonProperty("data")
    private CallbackData data;

    @Data
    public static class CallbackData {

        @JsonProperty("id")
        private String id;

        @JsonProperty("txid")
        private String txid;

        @JsonProperty("address")
        private String address;

        @JsonProperty("network")
        private String network;

        @JsonProperty("paid_amount") // Matching JSON key
        private String paidAmount;

        @JsonProperty("paid_currency") // Matching JSON key
        private String paidCurrency;

        @JsonProperty("received_amount") // Matching JSON key
        private String receivedAmount;

        @JsonProperty("received_currency") // Matching JSON key
        private String receivedCurrency;

        @JsonProperty("fee")
        private String fee;

        @JsonProperty("fee_currency") // Matching JSON key
        private String feeCurrency;

        @JsonProperty("status")
        private String status;

        @JsonProperty("channel_id") // Matching JSON key
        private String channelId;

        @JsonProperty("custom_id") // Matching JSON key
        private String customId;

        @JsonProperty("customer_id") // Matching JSON key
        private String customerId;

        @JsonProperty("risk")
        private Risk risk;

        @JsonProperty("hosted_page_url") // Matching JSON key
        private String hostedPageUrl;

        @JsonProperty("created_at") // Matching JSON key
        private String createdAt;

        @JsonProperty("exchange")
        private Exchange exchange;

        @JsonProperty("status_context") // Matching JSON key
        private String statusContext;

        @JsonProperty("refund_address") // Matching JSON key
        private String refundAddress;
    }

    @Data
    public static class Risk {

        @JsonProperty("score")
        private Double score;

        @JsonProperty("level")
        private String level;

        @JsonProperty("resource_name") // Matching JSON key
        private String resourceName;

        @JsonProperty("resource_category") // Matching JSON key
        private String resourceCategory;
    }

    @Data
    public static class Exchange {

        @JsonProperty("fee")
        private String fee;

        @JsonProperty("pair")
        private String pair;

        @JsonProperty("rate")
        private String rate;

        @JsonProperty("fee_currency") // Matching JSON key
        private String feeCurrency;
    }
}
