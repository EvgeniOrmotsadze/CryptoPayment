package com.radlane.payment.controller;

import com.radlane.payment.model.dto.ChannelPaymentResponse;
import com.radlane.payment.model.dto.CreateChannelPaymentRequest;
import com.radlane.payment.service.ChannelPaymentService;
import com.radlane.payment.service.CryptoCallbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChannelPaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChannelPaymentService channelPaymentService;

    @Mock
    private CryptoCallbackService cryptoCallbackService;

    @InjectMocks
    private ChannelPaymentController channelPaymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(channelPaymentController).build();
    }

    @Test
    void testCreateChannelPayment() throws Exception {
        CreateChannelPaymentRequest request = new CreateChannelPaymentRequest();
        request.setName("Test creation channel");
        request.setNetwork("bitcoin");
        request.setCustom_id("123");
        request.setPay_currency("BTC");
        request.setReceiver_currency("EUR");

        ChannelPaymentResponse response = new ChannelPaymentResponse("2",
                "tb1p5ntm33xngr48q526usc5rn7ex5jlzyet03cf23q7z4kwmkdusxmstykt29",
                "bitcoin",
                "enabled",
                "https://hosted-business-sandbox.cryptopay.me/channels/3aff372b-7b9d-40a9-89f5-4496af2c11f2");

        // Mock the service layer
        when(channelPaymentService.createChannelPayment(any(CreateChannelPaymentRequest.class)))
                .thenReturn(response);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/channels")
                        .contentType("application/json")
                        .content("{"
                                + "\"name\": \"Test creation channel\","
                                + "\"network\": \"bitcoin\","
                                + "\"custom_id\": \"123\","
                                + "\"pay_currency\": \"BTC\","
                                + "\"receiver_currency\": \"EUR\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("tb1p5ntm33xngr48q526usc5rn7ex5jlzyet03cf23q7z4kwmkdusxmstykt29"));
    }

    @Test
    void testHandleCallbackValidSignature() throws Exception {
        // Prepare test data
        String body = "callbackBody";
        String signature = "validSignature";

        // Mock the service layer
        when(cryptoCallbackService.verifySignature(body, signature)).thenReturn(true);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/channels/callback")
                        .contentType("application/json")
                        .content(body)
                        .header("X-Cryptopay-Signature", signature))
                .andExpect(status().isOk());

        // Verify service method was called
        verify(cryptoCallbackService, times(1)).processCallback(body);
    }

    @Test
    void testHandleCallbackInvalidSignature() throws Exception {
        // Prepare test data
        String body = "callbackBody";
        String signature = "invalidSignature";

        // Mock the service layer
        when(cryptoCallbackService.verifySignature(body, signature)).thenReturn(false);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/channels/callback")
                        .contentType("application/json")
                        .content(body)
                        .header("X-Cryptopay-Signature", signature))
                .andExpect(status().isForbidden());

        // Verify service method was not called
        verify(cryptoCallbackService, times(0)).processCallback(body);
    }
}