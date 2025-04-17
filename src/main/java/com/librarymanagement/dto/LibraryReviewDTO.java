package com.librarymanagement.dto;

import java.time.LocalDateTime;

public class LibraryReviewDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private int rating;
    private String reviewText;
    private LocalDateTime reviewDate;
    
    // Default constructor
    public LibraryReviewDTO() {}
    
    // Constructor with required fields
    public LibraryReviewDTO(Long userId, String title, int rating, String reviewText) {
        this.userId = userId;
        this.title = title;
        this.rating = rating;
        this.reviewText = reviewText;
    }
    
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getReviewText() {
        return reviewText;
    }
    
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    
    public LocalDateTime getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
} 