package com.librarymanagement.service;

import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    public String addDelivery(Long transactionId) {
        // Business logic to add delivery
        return "Delivery added successfully!";
    }

    public String getDeliveryStatus(Long id) {
        // Business logic to get delivery status
        return "Delivery status: Delivered";
    }
}
