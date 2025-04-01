package com.librarymanagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "$(Membership.ToLower())s")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
