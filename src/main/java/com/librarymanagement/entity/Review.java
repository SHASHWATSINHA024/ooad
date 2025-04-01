package com.librarymanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "$(Review.ToLower())s")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
