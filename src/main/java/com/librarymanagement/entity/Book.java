package com.librarymanagement.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")  // Use the "books" table in the database
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String author;
    
    private String isbn;
    
    @Column(length = 1000)
    private String description;
    
    private String category;  // ✅ Add missing category field
    
    private BigDecimal price;
    
    private int stock = 0;
    
    private int coinPrice = 0;
    
    private int borrowCount = 0;
    
    private LocalDateTime publishDate;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<BookReview> reviews = new HashSet<>();
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<BookTransaction> transactions = new HashSet<>();
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<WishlistItem> wishlistItems = new HashSet<>();
    
    // Constructors
    public Book() {}
    
    // Getters and setters
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

    public String getCategory() {  // ✅ Correct getter for category
        return category;
    }

    public void setCategory(String category) {  // ✅ Correct setter for category
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

    public Set<BookReview> getReviews() {
        return reviews;
    }

    public void setReviews(Set<BookReview> reviews) {
        this.reviews = reviews;
    }

    public Set<BookTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<BookTransaction> transactions) {
        this.transactions = transactions;
    }

    public Set<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistItems(Set<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }
}
