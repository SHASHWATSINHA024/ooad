package com.librarymanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "$(Transaction.ToLower())s")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
