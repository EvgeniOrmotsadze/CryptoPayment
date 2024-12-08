package com.radlane.payment.crypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radlane.payment.configuration.CryptoConfig;
import com.radlane.payment.crypto.util.HmacGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CryptoApiClient {

    private final WebClient webClient;

    private final CryptoConfig cryptoConfig;


    public CryptoApiClient(WebClient.Builder webClientBuilder, CryptoConfig cryptoConfig) {
        this.cryptoConfig = cryptoConfig;
        this.webClient = webClientBuilder.baseUrl(cryptoConfig.getApi().getUrl()).build();
    }

    public <T> T post(String uri, Object requestBody, Class<T> responseType) {
        String dateHeader = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now());
        String requestBodyJson;
        try {
            requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing request body", e);
        }

        String signature = generateAuthorizationHeader(uri, requestBodyJson, dateHeader);
        try {
            return webClient.post()
                    .uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, "HMAC " + cryptoConfig.getApi().getKey() + ":" + signature)
                    .header(HttpHeaders.DATE, dateHeader)
                    .bodyValue(requestBodyJson)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during API call", e);
        }
    }

    private String generateAuthorizationHeader(String uri, String requestBody, String dateHeader) {
        try {
            String canonicalString = String.join("\n",
                    HttpMethod.POST.name(),
                    HmacGenerator.generateMd5Hex(requestBody),
                    MediaType.APPLICATION_JSON_VALUE,
                    dateHeader,
                    uri
            );
            return HmacGenerator.generateHmac(canonicalString, cryptoConfig.getApi().getSecret());
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC signature", e);
        }
    }
}
