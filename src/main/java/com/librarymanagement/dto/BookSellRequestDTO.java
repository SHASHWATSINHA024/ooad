package com.librarymanagement.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookSellRequestDTO {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String userName;
    
    @NotBlank(message = "Book title is required")
    private String bookTitle;
    
    @NotBlank(message = "Book author is required")
    private String bookAuthor;
    
    private String isbn;
    
    private String bookCondition;
    
    @NotNull(message = "Asking price is required")
    @Min(value = 0, message = "Asking price must be non-negative")
    private BigDecimal askingPrice;
    
    private LocalDateTime requestDate;
    
    private String status;
    
    private String notes;
    
    private LocalDateTime reviewedDate;
    
    private Long reviewedById;
    
    private String reviewedByName;
    
    // Default constructor
    public BookSellRequestDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public String getBookAuthor() {
        return bookAuthor;
    }
    
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getBookCondition() {
        return bookCondition;
    }
    
    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }
    
    public BigDecimal getAskingPrice() {
        return askingPrice;
    }
    
    public void setAskingPrice(BigDecimal askingPrice) {
        this.askingPrice = askingPrice;
    }
    
    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getReviewedDate() {
        return reviewedDate;
    }
    
    public void setReviewedDate(LocalDateTime reviewedDate) {
        this.reviewedDate = reviewedDate;
    }
    
    public Long getReviewedById() {
        return reviewedById;
    }
    
    public void setReviewedById(Long reviewedById) {
        this.reviewedById = reviewedById;
    }
    
    public String getReviewedByName() {
        return reviewedByName;
    }
    
    public void setReviewedByName(String reviewedByName) {
        this.reviewedByName = reviewedByName;
    }
} 