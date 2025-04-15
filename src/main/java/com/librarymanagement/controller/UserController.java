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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
    }

    @GetMapping("/{id}/home")
    public ResponseEntity<?> getUserHomePage(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }

        Map<String, Object> homeData = new HashMap<>();
        homeData.put("topBooks", bookService.getTopBooks(10));
        homeData.put("recentBookTransaction", bookTransactionService.getRecentTransaction(id));
        homeData.put("currentBorrows", bookTransactionService.getCurrentBorrows(id));
        homeData.put("coins", userService.getUserCoins(id));

        return ResponseEntity.ok(homeData);
    }

    @GetMapping("/{id}/coins")
    public ResponseEntity<?> getUserCoins(@PathVariable Long id) {
        Integer coins = userService.getUserCoins(id);

        if (coins != null) {
            return ResponseEntity.ok(Map.of("coins", coins));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to borrow book"));
        }
    }

    @PostMapping("/{id}/books/{bookId}/buy")
    public ResponseEntity<?> buyBook(@PathVariable Long id, @PathVariable Long bookId,
                                     @RequestParam(defaultValue = "0") int coinsToUse) {
        BookTransactionDTO transactionDTO = new BookTransactionDTO();
        transactionDTO.setUserId(id);
        transactionDTO.setBookId(bookId);
        transactionDTO.setType("PURCHASE");
        transactionDTO.setCoinsUsed(coinsToUse);

        BookTransactionDTO transaction = bookTransactionService.createTransaction(transactionDTO);

        if (transaction != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to purchase book"));
        }
    }

    @PostMapping("/{id}/books/{bookId}/wishlist")
    public ResponseEntity<?> addToWishlist(@PathVariable Long id, @PathVariable Long bookId) {
        boolean added = wishlistService.addToWishlist(id, bookId);

        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Book added to wishlist"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to add book to wishlist"));
        }
    }

    @DeleteMapping("/{id}/books/{bookId}/wishlist")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long id, @PathVariable Long bookId) {
        boolean removed = wishlistService.removeFromWishlist(id, bookId);

        if (removed) {
            return ResponseEntity.ok(Map.of("message", "Book removed from wishlist"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to remove book from wishlist"));
        }
    }

    @GetMapping("/{id}/wishlist")
    public ResponseEntity<?> getWishlist(@PathVariable Long id) {
        List<BookDTO> wishlist = wishlistService.getUserWishlist(id);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/{id}/books/{bookId}/review")
    public ResponseEntity<?> reviewBook(@PathVariable Long id, @PathVariable Long bookId,
                                        @Valid @RequestBody BookReviewDTO reviewDTO) {
        reviewDTO.setUserId(id);
        reviewDTO.setBookId(bookId);

        BookReviewDTO savedReview = bookReviewService.addReview(reviewDTO);

        if (savedReview != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to add review"));
        }
    }

    @PostMapping("/{id}/sell-book")
    public ResponseEntity<?> sellBook(@PathVariable Long id,
                                      @Valid @RequestBody BookSellRequestDTO sellRequestDTO) {
        sellRequestDTO.setUserId(id);
        BookSellRequestDTO savedRequest = bookSellRequestService.createSellRequest(sellRequestDTO);

        if (savedRequest != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Book sell request submitted successfully",
                    "request", savedRequest));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to submit book sell request"));
        }
    }

    @GetMapping("/{id}/sell-requests")
    public ResponseEntity<?> getSellRequests(@PathVariable Long id) {
        List<BookSellRequestDTO> requests = bookSellRequestService.getUserRequests(id);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getUserTransactions(@PathVariable Long id) {
        List<BookTransactionDTO> transactions = bookTransactionService.getTransactionsByUser(id);
        return ResponseEntity.ok(transactions);
    }
}
