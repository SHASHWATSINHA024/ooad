package com.librarymanagement.entity;

import javax.persistence.*;

@Entity
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private boolean isActive;

    // Getters and setters
}
