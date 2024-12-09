package com.radlane.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radlane.payment.crypto.util.CallbackVerifier;
import com.radlane.payment.model.PaymentStatus;
import com.radlane.payment.model.dto.ChannelPaymentCallbackRequest;
import com.radlane.payment.model.entity.Payment;
import com.radlane.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CryptoCallbackServiceTest {

    @Mock
    private CallbackVerifier callbackVerifier;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PaymentRepository paymentRepository;

    // Inject mocks into the service
    @InjectMocks
    private CryptoCallbackServiceImpl cryptoCallbackService;

    private ChannelPaymentCallbackRequest.CallbackData callbackData;
    private Payment payment;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        cryptoCallbackService = new CryptoCallbackServiceImpl(callbackVerifier, objectMapper, paymentRepository);

        // Sample CallbackData to be used in tests
        callbackData = new ChannelPaymentCallbackRequest.CallbackData();
        callbackData.setId("12345");
        callbackData.setStatus("completed");
        callbackData.setStatusContext("none");
        callbackData.setPaidAmount("100.00");
        callbackData.setPaidCurrency("USD");
        callbackData.setReceivedAmount("90.00");
        callbackData.setReceivedCurrency("USD");
        callbackData.setFee("5.00");
        callbackData.setFeeCurrency("USD");

        // Mock Payment entity
        payment = new Payment();
        payment.setPaymentId("12345");
        payment.setStatus(PaymentStatus.PENDING);
    }

    @Test
    void testVerifySignature_ShouldReturnTrue_WhenSignatureIsValid() {
        // Mock callbackVerifier to return true for valid signature
        when(callbackVerifier.verify(anyString(), anyString())).thenReturn(true);
        boolean result = cryptoCallbackService.verifySignature("body", "valid_signature");
        assertTrue(result);
        verify(callbackVerifier).verify(anyString(), anyString());
    }

    @Test
    void testProcessCallback_ShouldProcessCallback_WhenPaymentDoesNotExist() throws Exception {
        // Mock objectMapper to return mock CallbackData from the body
        ChannelPaymentCallbackRequest channelPaymentCallbackRequest = new ChannelPaymentCallbackRequest();
        callbackData.setStatus("pending");
        channelPaymentCallbackRequest.setData(callbackData);

        when(objectMapper.readValue(anyString(), eq(ChannelPaymentCallbackRequest.class)))
                .thenReturn(channelPaymentCallbackRequest);

        // Mock paymentRepository to return Optional.empty(), implying payment does not exist
        when(paymentRepository.findByPaymentId(callbackData.getId()))
                .thenReturn(Optional.empty());

        // Mock paymentRepository to save a new payment
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Call the method under test
        cryptoCallbackService.processCallback("callback_body");

        // Verify that the payment was saved and the status was set to COMPLETED
        Mockito.verify(paymentRepository, Mockito.times(2)).save(Mockito.any(Payment.class));
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void testProcessCallback_ShouldHandleCompletedStatus() throws Exception {
        // Simulate the scenario where the payment already exists
        when(paymentRepository.findByPaymentId(callbackData.getId()))
                .thenReturn(Optional.of(payment));


        ChannelPaymentCallbackRequest channelPaymentCallbackRequest = new ChannelPaymentCallbackRequest();
        channelPaymentCallbackRequest.setData(callbackData);

        when(objectMapper.readValue(anyString(), eq(ChannelPaymentCallbackRequest.class)))
                .thenReturn(channelPaymentCallbackRequest);
        // Call the method under test
        cryptoCallbackService.processCallback("callback_body");

        // Verify that payment's status was updated to COMPLETED
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(new BigDecimal("100.00"), payment.getPaidAmount());
        assertEquals("USD", payment.getPaidCurrency());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testProcessCallback_ShouldHandleOnHoldStatus() throws Exception {
        // Create a mock or actual object for ChannelPaymentCallbackRequest
        ChannelPaymentCallbackRequest channelPaymentCallbackRequest = new ChannelPaymentCallbackRequest();
        ChannelPaymentCallbackRequest.CallbackData callbackData = new ChannelPaymentCallbackRequest.CallbackData();
        callbackData.setId("payment_id");
        callbackData.setStatus("on_hold");
        callbackData.setStatusContext("illicit_resource");
        channelPaymentCallbackRequest.setData(callbackData);

        // Mock the payment repository
        when(paymentRepository.findByPaymentId(callbackData.getId()))
                .thenReturn(Optional.of(payment));

        // Mock the JSON parsing if necessary
        when(objectMapper.readValue(anyString(), eq(ChannelPaymentCallbackRequest.class)))
                .thenReturn(channelPaymentCallbackRequest);

        // Call the method under test
        cryptoCallbackService.processCallback("callback_body");

        // Verify that no changes are made to the payment
        verify(paymentRepository, never()).save(any(Payment.class));
    }


    @Test
    void testProcessCallback_ShouldSkipProcessingWhenAlreadyProcessed() throws Exception {
        // Mock the scenario where payment already exists and is completed
        payment.setStatus(PaymentStatus.COMPLETED);
        when(paymentRepository.findByPaymentId("test-id"))
                .thenReturn(Optional.of(payment));

        // Mock deserialization
        String validCallbackBody = "{ \"id\": \"test-id\", \"data\": { /* ... */ } }";
        ChannelPaymentCallbackRequest callbackRequestMock = new ChannelPaymentCallbackRequest();
        callbackData.setStatus("completed");
        callbackData.setId("test-id");
        callbackRequestMock.setData(callbackData);
        when(objectMapper.readValue(validCallbackBody, ChannelPaymentCallbackRequest.class))
                .thenReturn(callbackRequestMock);

        // Call the method under test
        cryptoCallbackService.processCallback(validCallbackBody);

        verify(paymentRepository, never()).save(any(Payment.class));

    }


    @Test
    void testProcessCallback_ShouldCreateNewPayment_WhenPaymentDoesNotExist() throws Exception {
        // Mock the scenario where payment does not exist in the repository
        when(paymentRepository.findByPaymentId(callbackData.getId()))
                .thenReturn(Optional.empty());

        // Prepare ChannelPaymentCallbackRequest and mock objectMapper to return it
        ChannelPaymentCallbackRequest channelPaymentCallbackRequest = new ChannelPaymentCallbackRequest();
        channelPaymentCallbackRequest.setData(callbackData);

        when(objectMapper.readValue(anyString(), eq(ChannelPaymentCallbackRequest.class)))
                .thenReturn(channelPaymentCallbackRequest);

        // Call the method under test
        cryptoCallbackService.processCallback("callbackBody");

        // Verify that a new payment is created and saved
        Mockito.verify(paymentRepository, Mockito.times(2)).save(Mockito.any(Payment.class));
    }
}
