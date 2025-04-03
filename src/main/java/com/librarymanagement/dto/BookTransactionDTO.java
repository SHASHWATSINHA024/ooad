package com.librarymanagement.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookTransactionDTO {
    private Long id;
    
    @NotNull(message = "Book ID is required")
    private Long bookId;
    
    private String bookTitle;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String userName;
    
    @NotNull(message = "Transaction type is required")
    private String type;
    
    private LocalDateTime transactionDate;
    
    private LocalDateTime dueDate;
    
    private LocalDateTime returnDate;
    
    @Min(value = 0, message = "Coins used must be non-negative")
    private int coinsUsed;
    
    private boolean isPaid;
    
    private String notes;
    
    private Long daysLeft;
    
    private boolean isOverdue;
    
    // Default constructor
    public BookTransactionDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDateTime getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
    
    public int getCoinsUsed() {
        return coinsUsed;
    }
    
    public void setCoinsUsed(int coinsUsed) {
        this.coinsUsed = coinsUsed;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
    
    public void setPaid(boolean paid) {
        isPaid = paid;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Long getDaysLeft() {
        return daysLeft;
    }
    
    public void setDaysLeft(Long daysLeft) {
        this.daysLeft = daysLeft;
    }
    
    public boolean isOverdue() {
        return isOverdue;
    }
    
    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
} 