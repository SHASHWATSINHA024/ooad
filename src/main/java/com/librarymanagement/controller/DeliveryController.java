package com.librarymanagement.controller;

import com.librarymanagement.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/add")
    public String addDelivery(@RequestParam Long transactionId) {
        return deliveryService.addDelivery(transactionId);
    }

    @GetMapping("/status/{id}")
    public String getDeliveryStatus(@PathVariable Long id) {
        return deliveryService.getDeliveryStatus(id);
    }
}
