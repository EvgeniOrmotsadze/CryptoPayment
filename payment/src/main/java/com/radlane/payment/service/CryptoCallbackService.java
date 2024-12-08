package com.radlane.payment.service;

public interface CryptoCallbackService {

     boolean verifySignature(String body, String signature);
     void processCallback(String body);

}

