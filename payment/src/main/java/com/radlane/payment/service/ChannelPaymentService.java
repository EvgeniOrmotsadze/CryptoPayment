package com.radlane.payment.service;

import com.radlane.payment.model.dto.ChannelPaymentResponse;
import com.radlane.payment.model.dto.CreateChannelPaymentRequest;

public interface ChannelPaymentService {


    ChannelPaymentResponse createChannelPayment(CreateChannelPaymentRequest request);


}
