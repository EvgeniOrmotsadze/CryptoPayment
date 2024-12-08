package com.radlane.payment.crypto.util;


import com.radlane.payment.configuration.CryptoConfig;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class CallbackVerifier {

    private final CryptoConfig cryptoConfig;

    public CallbackVerifier(CryptoConfig cryptoConfig) {
        this.cryptoConfig = cryptoConfig;
    }


    public boolean verify(String body, String signature) {
        try {
            String expectedSignature = generateSignature(body);
            return secureCompare(signature, expectedSignature);
        } catch (Exception e) {
            throw new RuntimeException("Error verifying signature", e);
        }
    }

    public String generateSignature(String body) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(cryptoConfig.getCallback().getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hmacBytes);
    }

    private boolean secureCompare(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() != str2.length()) return false;

        int result = 0;
        for (int i = 0; i < str1.length(); i++) {
            result |= str1.charAt(i) ^ str2.charAt(i);
        }
        return result == 0;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
