package com.librarymanagement.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_sell_requests")
public class BookSellRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String bookTitle;
    
    @Column(nullable = false)
    private String bookAuthor;
    
    private String isbn;
    
    private String bookCondition;
    
    private BigDecimal askingPrice;
    
    private LocalDateTime requestDate = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;
    
    private String notes;
    
    private LocalDateTime reviewedDate;
    
    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian reviewedBy;
    
    // Enum for request status
    public enum RequestStatus {
        PENDING, APPROVED, REJECTED
    }
    
    // Constructors
    public BookSellRequest() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    public RequestStatus getStatus() {
        return status;
    }
    
    public void setStatus(RequestStatus status) {
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
    
    public Librarian getReviewedBy() {
        return reviewedBy;
    }
    
    public void setReviewedBy(Librarian reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
} 