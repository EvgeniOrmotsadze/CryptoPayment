package com.radlane.payment.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "crypto")
public class CryptoConfig {

    private Api api;
    private Callback callback;
    private Allowed allowed;

    @Data
    public static class Api {
        private String key;
        private String secret;
        private String url;
    }

    @Data
    public static class Callback {
        private String secret;
    }

    @Data
    public static class Allowed {
        private String ip;
    }
}
