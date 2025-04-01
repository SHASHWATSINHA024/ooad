package com.librarymanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "$(User.ToLower())s")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
