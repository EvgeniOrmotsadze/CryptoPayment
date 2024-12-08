package com.radlane.payment.crypto.util;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HmacGenerator {

    /**
     * Generates an HMAC signature for the given data using the provided secret key.
     *
     * @param data   The data to be signed.
     * @param secret The secret key used for signing.
     * @return The generated HMAC signature.
     * @throws NoSuchAlgorithmException If the HMAC algorithm is not available.
     * @throws InvalidKeyException      If the secret key is invalid.
     */
    public static String generateHmac(String data, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    /**
     * Generates the MD5 hash of the given string in hexadecimal format.
     *
     * @param str The string to be hashed.
     * @return The MD5 hash in hexadecimal format.
     * @throws NoSuchAlgorithmException If the MD5 algorithm is not available.
     */
    public static String generateMd5Hex(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array.
     * @return The hexadecimal string.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
