package com.librarymanagement.dto;

import com.librarymanagement.entity.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private String category;
    private BigDecimal price;
    private int stock;
    private int coinPrice;
    private int borrowCount;
    private LocalDateTime publishDate;
    private Double averageRating;
    private int availableCopies;
    private int totalCopies;

    // Default Constructor
    public BookDTO() {}

    // Constructor with all parameters
    public BookDTO(Long id, String title, String author, String isbn, String description, String category,
                   BigDecimal price, int stock, int coinPrice, int borrowCount, LocalDateTime publishDate,
                   Double averageRating, int availableCopies, int totalCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.coinPrice = coinPrice;
        this.borrowCount = borrowCount;
        this.publishDate = publishDate;
        this.averageRating = averageRating;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(int coinPrice) {
        this.coinPrice = coinPrice;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    // Utility method to create a BookDTO from a Book entity
    public static BookDTO fromBook(Book book, Double avgRating) {
        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getDescription(),
            book.getCategory(),
            book.getPrice(),
            book.getStock(),
            book.getCoinPrice(),
            book.getBorrowCount(),
            book.getPublishDate(),
            avgRating,
            book.getAvailableCopies(),
            book.getTotalCopies()
        );
    }
}
