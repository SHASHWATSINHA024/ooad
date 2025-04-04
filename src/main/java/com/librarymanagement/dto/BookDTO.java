package com.librarymanagement.dto;

public class BookDTO {
    private String title;
    private String author;
    private String category;
    private double price;

    // Constructor
    public BookDTO(String title, String author, String category, double price) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
}
