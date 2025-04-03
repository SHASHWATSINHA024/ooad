package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.dto.BookReviewDTO;
import com.librarymanagement.dto.BookSellRequestDTO;
import com.librarymanagement.dto.BookTransactionDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.BookReviewService;
import com.librarymanagement.service.BookSellRequestService;
import com.librarymanagement.service.BookService;
import com.librarymanagement.service.BookTransactionService;
import com.librarymanagement.service.UserService;
import com.librarymanagement.service.WishlistService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookTransactionService bookTransactionService;
    
    @Autowired
    private BookReviewService bookReviewService;
    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private BookSellRequestService bookSellRequestService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @GetMapping("/{id}/home")
    public ResponseEntity<?> getUserHomePage(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        
        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        Map<String, Object> homeData = new HashMap<>();
        
        // Top books of the month
        List<BookDTO> topBooks = bookService.getTopBooks(10);
        homeData.put("topBooks", topBooks);
        
        // Most recent book taken
        BookTransactionDTO recentTransaction = bookTransactionService.getRecentTransaction(id);
        homeData.put("recentBookTransaction", recentTransaction);
        
        // Current borrowed books with days left to return
        List<BookTransactionDTO> currentBorrows = bookTransactionService.getCurrentBorrows(id);
        homeData.put("currentBorrows", currentBorrows);
        
        // User coins
        Integer coins = userService.getUserCoins(id);
        homeData.put("coins", coins);
        
        return ResponseEntity.ok(homeData);
    }
    
    @GetMapping("/{id}/coins")
    public ResponseEntity<?> getUserCoins(@PathVariable Long id) {
        Integer coins = userService.getUserCoins(id);
        
        if (coins != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("coins", coins);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @PostMapping("/{id}/books/{bookId}/borrow")
    public ResponseEntity<?> borrowBook(@PathVariable Long id, @PathVariable Long bookId) {
        BookTransactionDTO transactionDTO = new BookTransactionDTO();
        transactionDTO.setUserId(id);
        transactionDTO.setBookId(bookId);
        transactionDTO.setType("BORROW");
        
        BookTransactionDTO transaction = bookTransactionService.createTransaction(transactionDTO);
        
        if (transaction != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to borrow book");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/{id}/books/{bookId}/buy")
    public ResponseEntity<?> buyBook(
            @PathVariable Long id, 
            @PathVariable Long bookId, 
            @RequestParam(required = false, defaultValue = "0") int coinsToUse) {
        
        BookTransactionDTO transactionDTO = new BookTransactionDTO();
        transactionDTO.setUserId(id);
        transactionDTO.setBookId(bookId);
        transactionDTO.setType("PURCHASE");
        transactionDTO.setCoinsUsed(coinsToUse);
        
        BookTransactionDTO transaction = bookTransactionService.createTransaction(transactionDTO);
        
        if (transaction != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to purchase book");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/{id}/books/{bookId}/wishlist")
    public ResponseEntity<?> addToWishlist(@PathVariable Long id, @PathVariable Long bookId) {
        boolean added = wishlistService.addToWishlist(id, bookId);
        
        if (added) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book added to wishlist");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to add book to wishlist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @DeleteMapping("/{id}/books/{bookId}/wishlist")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long id, @PathVariable Long bookId) {
        boolean removed = wishlistService.removeFromWishlist(id, bookId);
        
        if (removed) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book removed from wishlist");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to remove book from wishlist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/{id}/wishlist")
    public ResponseEntity<?> getWishlist(@PathVariable Long id) {
        List<BookDTO> wishlist = wishlistService.getUserWishlist(id);
        return ResponseEntity.ok(wishlist);
    }
    
    @PostMapping("/{id}/books/{bookId}/review")
    public ResponseEntity<?> reviewBook(
            @PathVariable Long id, 
            @PathVariable Long bookId, 
            @Valid @RequestBody BookReviewDTO reviewDTO) {
        
        reviewDTO.setUserId(id);
        reviewDTO.setBookId(bookId);
        
        BookReviewDTO savedReview = bookReviewService.addReview(reviewDTO);
        
        if (savedReview != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to add review");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/{id}/sell-book")
    public ResponseEntity<?> sellBook(
            @PathVariable Long id, 
            @Valid @RequestBody BookSellRequestDTO sellRequestDTO) {
        
        sellRequestDTO.setUserId(id);
        
        BookSellRequestDTO savedRequest = bookSellRequestService.createSellRequest(sellRequestDTO);
        
        if (savedRequest != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book sell request submitted successfully");
            response.put("request", savedRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to submit book sell request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/{id}/sell-requests")
    public ResponseEntity<?> getSellRequests(@PathVariable Long id) {
        List<BookSellRequestDTO> requests = bookSellRequestService.getUserSellRequests(id);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getUserTransactions(@PathVariable Long id) {
        List<BookTransactionDTO> transactions = bookTransactionService.getTransactionsByUser(id);
        return ResponseEntity.ok(transactions);
    }
}
