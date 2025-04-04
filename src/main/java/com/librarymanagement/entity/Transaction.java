package com.librarymanagement.entity;

import javax.persistence.*;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long bookId;
    private String status;  // e.g., "borrowed", "returned"

    // Getters and setters
}
