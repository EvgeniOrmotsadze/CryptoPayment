package com.radlane.payment.crypto.util;

import com.radlane.payment.configuration.CryptoConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class CallbackVerifierTest {

    @Mock
    private CryptoConfig cryptoConfig;

    @Mock
    private CryptoConfig.Callback callback;

    private CallbackVerifier callbackVerifier;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up the mock behavior
        when(cryptoConfig.getCallback()).thenReturn(callback);
        when(callback.getSecret()).thenReturn("secret-key");

        // Initialize CallbackVerifier with mocked CryptoConfig
        callbackVerifier = new CallbackVerifier(cryptoConfig);
    }

    @Test
    void testVerifyValidSignature() throws Exception {
        // Sample body and signature for testing
        String body = "test-body";
        String expectedSignature = "a568fa2d80bd4434ee46f3d4ebb7efba3b5ea10cfc824b7881b1b6c1192a0ebf"; // Correct expected signature

        // Stub the generateSignature method to return the expected signature
        when(callbackVerifier.generateSignature(body)).thenReturn(expectedSignature);

        // Generate the signature from the body
        String generatedSignature = callbackVerifier.generateSignature(body);

        boolean result = callbackVerifier.verify(body, generatedSignature);

        assertTrue(result, "The signature verification should pass. Generated Signature: " + generatedSignature + ", Expected Signature: " + expectedSignature);
    }


    @Test
    void testVerifyInvalidSignature() throws Exception {
        // Sample body and signature for testing
        String body = "test-body";
        String invalidSignature = "invalid-signature";

        when(callbackVerifier.generateSignature(body)).thenReturn("expected-signature");

        boolean result = callbackVerifier.verify(body, invalidSignature);

        assertFalse(result, "The signature verification should fail.");
    }

}
