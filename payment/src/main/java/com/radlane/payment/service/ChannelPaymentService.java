package com.radlane.payment.service;

import com.radlane.payment.crypto.Channels;
import com.radlane.payment.model.dto.CreateChannelPaymentResponse;
import com.radlane.payment.model.dto.CreateChannelPaymentRequest;
import com.radlane.payment.model.dto.ChannelPaymentResponse;
import com.radlane.payment.model.entity.CryptoChannel;
import com.radlane.payment.repository.ChannelRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelPaymentService {

    private final ChannelRepository channelRepository;
    private final Channels channels;

    @Autowired
    public ChannelPaymentService(ChannelRepository channelRepository, Channels channels) {
        this.channelRepository = channelRepository;
        this.channels = channels;
    }

    //  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public ChannelPaymentResponse createChannelPayment(CreateChannelPaymentRequest request) {
        try {
            // Call the external API to create the channel
            CreateChannelPaymentResponse response = channels.createChannel(request);
            if (response != null) {
                // Map the response to a CryptoChannel entity
                CryptoChannel cryptoChannel = getCryptoChannel(request, response);
                // Save the entity to the database
                cryptoChannel = channelRepository.save(cryptoChannel);
                return new ChannelPaymentResponse(
                        String.valueOf(cryptoChannel.getId()),
                        cryptoChannel.getAddress(),
                        cryptoChannel.getNetwork(),
                        cryptoChannel.getStatus(),
                        cryptoChannel.getHostedPageUrl());

            } else {
                return new ChannelPaymentResponse("Error: Null response from external API");
            }
        } catch (Exception e) {
            return new ChannelPaymentResponse("Error: " + e.getMessage());
        }
    }

    @NotNull
    private static CryptoChannel getCryptoChannel(CreateChannelPaymentRequest request, CreateChannelPaymentResponse response) {
        CryptoChannel cryptoChannel = new CryptoChannel();
        cryptoChannel.setCustomerId(request.getCustom_id());
        cryptoChannel.setReceiverCurrency(request.getPay_currency());  // Cryptocurrency type from the request
        cryptoChannel.setAddress(response.getData().getAddress());  // Address from response
        cryptoChannel.setName(response.getData().getName());
        cryptoChannel.setDescription(response.getData().getDescription());
        cryptoChannel.setReceiverCurrency(response.getData().getReceiverCurrency());
        cryptoChannel.setPayCurrency(response.getData().getPayCurrency());
        cryptoChannel.setNetwork(response.getData().getNetwork());
        cryptoChannel.setProjectId(response.getData().getProjectId());
        cryptoChannel.setCustomId(response.getData().getCustomId());
        cryptoChannel.setUri(response.getData().getUri());
        cryptoChannel.setHostedPageUrl(response.getData().getHostedPageUrl());
        cryptoChannel.setStatus(response.getData().getStatus());
        return cryptoChannel;
    }

}
