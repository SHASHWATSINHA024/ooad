package com.librarymanagement.entity;

import javax.persistence.*;

@Entity
public class BookDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long transactionId;
    private String status;

    // Getters and setters
}
