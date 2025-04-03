package com.librarymanagement.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "membership_type")
    private String membershipType;
    
    @Column(name = "membership_start_date")
    private LocalDateTime membershipStartDate;
    
    @Column(name = "membership_end_date")
    private LocalDateTime membershipEndDate;
    
    private int coins = 0;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BookTransaction> transactions = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<BookReview> reviews = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<WishlistItem> wishlist = new HashSet<>();

    // Enum for user status
    public enum UserStatus {
        PENDING, ACTIVE, SUSPENDED
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public LocalDateTime getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(LocalDateTime membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public LocalDateTime getMembershipEndDate() {
        return membershipEndDate;
    }

    public void setMembershipEndDate(LocalDateTime membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<BookTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BookTransaction> transactions) {
        this.transactions = transactions;
    }

    public Set<BookReview> getReviews() {
        return reviews;
    }

    public void setReviews(Set<BookReview> reviews) {
        this.reviews = reviews;
    }

    public Set<WishlistItem> getWishlist() {
        return wishlist;
    }

    public void setWishlist(Set<WishlistItem> wishlist) {
        this.wishlist = wishlist;
    }
}
