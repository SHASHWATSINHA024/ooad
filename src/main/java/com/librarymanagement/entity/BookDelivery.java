package com.librarymanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "$(BookDelivery.ToLower())s")
public class BookDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
