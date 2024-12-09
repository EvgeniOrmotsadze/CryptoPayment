package com.radlane.payment.service;

import com.radlane.payment.crypto.Channels;
import com.radlane.payment.exception.CustomPaymentException;
import com.radlane.payment.model.dto.ChannelPaymentResponse;
import com.radlane.payment.model.dto.CreateChannelPaymentRequest;
import com.radlane.payment.model.dto.CreateChannelPaymentResponse;
import com.radlane.payment.model.entity.CryptoChannel;
import com.radlane.payment.repository.ChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ChannelPaymentServiceTest {
    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private Channels channels;

    @InjectMocks
    private ChannelPaymentServiceImpl channelPaymentService;

    private CreateChannelPaymentRequest request;
    private CreateChannelPaymentResponse externalResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize request and external response
        request = new CreateChannelPaymentRequest();
        request.setCustom_id("customer_123");
        request.setPay_currency("BTC");

        externalResponse = new CreateChannelPaymentResponse();
        externalResponse.setData(new CreateChannelPaymentResponse.ChannelDataDto());
        externalResponse.getData().setAddress("cryptoAddress");
        externalResponse.getData().setName("CryptoChannel");
        externalResponse.getData().setReceiverCurrency("BTC");
        externalResponse.getData().setPayCurrency("BTC");
        externalResponse.getData().setNetwork("BTC Network");
        externalResponse.getData().setProjectId("project_001");
        externalResponse.getData().setCustomId("custom_123");
        externalResponse.getData().setUri("uri");
        externalResponse.getData().setHostedPageUrl("hostedPageUrl");
        externalResponse.getData().setStatus("active");
    }

    @Test
    void testCreateChannelPayment_Success() {
        // Arrange
        when(channels.createChannel(request)).thenReturn(externalResponse);
        when(channelRepository.save(any(CryptoChannel.class))).thenReturn(new CryptoChannel());

        // Act
        ChannelPaymentResponse response = channelPaymentService.createChannelPayment(request);

        // Assert
        assertNotNull(response);
        assertNull(response.getErrorMessage());
    }

    @Test
    void testCreateChannelPayment_NullResponseFromAPI() {
        // Arrange
        when(channels.createChannel(request)).thenReturn(null);

        // Act
        ChannelPaymentResponse response = channelPaymentService.createChannelPayment(request);

        // Assert
        assertEquals("Error: Null response from external API", response.getErrorMessage());
    }

    @Test
    void testCreateChannelPayment_Exception() {
        // Arrange
        when(channels.createChannel(request)).thenThrow(new CustomPaymentException("API error", HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        CustomPaymentException exception = assertThrows(CustomPaymentException.class, () -> {
            channelPaymentService.createChannelPayment(request);
        });

        // Validate the exception message
        assertEquals("Error: API error", exception.getMessage());
    }
}

