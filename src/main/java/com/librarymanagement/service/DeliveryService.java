package com.librarymanagement.service;

import com.librarymanagement.entity.BookDelivery;
import com.librarymanagement.repository.BookDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryService {

    @Autowired
    private BookDeliveryRepository bookDeliveryRepository;

    public String addDelivery(Long transactionId) {
        // Create a new delivery record with status "In Progress"
        BookDelivery delivery = new BookDelivery(transactionId, "In Progress");
        bookDeliveryRepository.save(delivery);
        return "Delivery added successfully for transaction ID: " + transactionId;
    }

    public String getDeliveryStatus(Long id) {
        Optional<BookDelivery> delivery = bookDeliveryRepository.findById(id);
        return delivery.map(bookDelivery -> "Delivery Status: " + bookDelivery.getStatus())
                       .orElse("Delivery not found for ID: " + id);
    }
}
