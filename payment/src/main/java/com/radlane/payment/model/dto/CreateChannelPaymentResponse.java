package com.radlane.payment.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateChannelPaymentResponse {
    @JsonProperty("data")
    private ChannelDataDto data;

    @Override
    public String toString() {
        return "ApiResponseWrapperDto{" +
                "data=" + data +
                '}';
    }

    @Data
    public static class ChannelDataDto {
        @JsonProperty("id")
        private String id;

        @JsonProperty("status")
        private String status;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("receiver_currency")
        private String receiverCurrency;

        @JsonProperty("pay_currency")
        private String payCurrency;

        @JsonProperty("address")
        private String address;

        @JsonProperty("network")
        private String network;

        @JsonProperty("project_id")
        private String projectId;

        @JsonProperty("custom_id")
        private String customId;

        @JsonProperty("customer_id")
        private String customerId;

        @JsonProperty("uri")
        private String uri;

        @JsonProperty("hosted_page_url")
        private String hostedPageUrl;


        @Override
        public String toString() {
            return "ChannelDataDto{" +
                    "id='" + id + '\'' +
                    ", status='" + status + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", receiverCurrency='" + receiverCurrency + '\'' +
                    ", payCurrency='" + payCurrency + '\'' +
                    ", address='" + address + '\'' +
                    ", network='" + network + '\'' +
                    ", projectId='" + projectId + '\'' +
                    ", customId='" + customId + '\'' +
                    ", customerId='" + customerId + '\'' +
                    ", uri='" + uri + '\'' +
                    ", hostedPageUrl='" + hostedPageUrl + '\'' +
                    '}';
        }
    }

}
