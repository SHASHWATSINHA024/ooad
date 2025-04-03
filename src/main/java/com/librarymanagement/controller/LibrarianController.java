package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.dto.BookOrderDTO;
import com.librarymanagement.dto.BookReviewDTO;
import com.librarymanagement.dto.BookSellRequestDTO;
import com.librarymanagement.dto.BookTransactionDTO;
import com.librarymanagement.dto.LibrarianDTO;
import com.librarymanagement.dto.MembershipRequestDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.BookOrderService;
import com.librarymanagement.service.BookReviewService;
import com.librarymanagement.service.BookSellRequestService;
import com.librarymanagement.service.BookService;
import com.librarymanagement.service.BookTransactionService;
import com.librarymanagement.service.LibrarianService;
import com.librarymanagement.service.MembershipRequestService;
import com.librarymanagement.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/librarians")
public class LibrarianController {

    @Autowired
    private LibrarianService librarianService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookTransactionService bookTransactionService;
    
    @Autowired
    private MembershipRequestService membershipRequestService;
    
    @Autowired
    private BookReviewService bookReviewService;
    
    @Autowired
    private BookSellRequestService bookSellRequestService;
    
    @Autowired
    private BookOrderService bookOrderService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getLibrarianById(@PathVariable Long id) {
        LibrarianDTO librarian = librarianService.getLibrarianById(id);
        
        if (librarian != null) {
            return ResponseEntity.ok(librarian);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Librarian not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @GetMapping("/{id}/home")
    public ResponseEntity<?> getLibrarianHomePage(@PathVariable Long id) {
        LibrarianDTO librarian = librarianService.getLibrarianById(id);
        
        if (librarian == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Librarian not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        Map<String, Object> homeData = new HashMap<>();
        
        // Get current members (active users)
        int currentYear = Year.now().getValue();
        
        // Get earnings for the current year
        Integer annualEarnings = librarianService.getAnnualEarnings(currentYear);
        homeData.put("annualEarnings", annualEarnings != null ? annualEarnings : 0);
        
        // Get monthly earnings for the current year
        Map<Month, Integer> monthlyEarnings = librarianService.getMonthlyEarnings(currentYear);
        homeData.put("monthlyEarnings", monthlyEarnings);
        
        // Get defaulted members (with overdue books)
        List<BookTransactionDTO> overdueBooks = bookTransactionService.getOverdueBooks();
        homeData.put("overdueBooks", overdueBooks);
        
        // Get current book stock
        List<BookDTO> books = bookService.getAllBooks();
        homeData.put("books", books);
        
        // Get pending membership requests
        List<MembershipRequestDTO> membershipRequests = membershipRequestService.getPendingRequests();
        homeData.put("pendingMembershipRequests", membershipRequests);
        
        // Get pending book sell requests
        List<BookSellRequestDTO> bookSellRequests = bookSellRequestService.getPendingSellRequests();
        homeData.put("pendingBookSellRequests", bookSellRequests);
        
        return ResponseEntity.ok(homeData);
    }
    
    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers() {
        List<UserDTO> members = userService.getAllActiveUsers();
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/defaulted-members")
    public ResponseEntity<?> getDefaultedMembers() {
        List<BookTransactionDTO> overdueBooks = bookTransactionService.getOverdueBooks();
        return ResponseEntity.ok(overdueBooks);
    }
    
    @GetMapping("/books/stock")
    public ResponseEntity<?> getBookStock() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    @PostMapping("/books")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        String result = bookService.addBook(bookDTO);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        String result = bookService.updateBook(id, bookDTO);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        String result = bookService.deleteBook(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/membership-requests")
    public ResponseEntity<?> getMembershipRequests() {
        List<MembershipRequestDTO> requests = membershipRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/membership-requests/pending")
    public ResponseEntity<?> getPendingMembershipRequests() {
        List<MembershipRequestDTO> requests = membershipRequestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }
    
    @PostMapping("/membership-requests/{id}/approve")
    public ResponseEntity<?> approveMembershipRequest(
            @PathVariable Long id, 
            @RequestParam Long librarianId,
            @RequestParam(required = false) String notes) {
        
        MembershipRequestDTO request = membershipRequestService.approveRequest(id, librarianId, notes);
        
        if (request != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Membership request approved");
            response.put("request", request);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to approve membership request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/membership-requests/{id}/reject")
    public ResponseEntity<?> rejectMembershipRequest(
            @PathVariable Long id, 
            @RequestParam Long librarianId,
            @RequestParam(required = false) String notes) {
        
        MembershipRequestDTO request = membershipRequestService.rejectRequest(id, librarianId, notes);
        
        if (request != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Membership request rejected");
            response.put("request", request);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to reject membership request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviews() {
        List<BookReviewDTO> reviews = bookReviewService.getRecentReviews(50);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<?> getBookReviews(@PathVariable Long bookId) {
        List<BookReviewDTO> reviews = bookReviewService.getBookReviews(bookId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/sell-requests")
    public ResponseEntity<?> getAllSellRequests() {
        List<BookSellRequestDTO> requests = bookSellRequestService.getAllSellRequests();
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/sell-requests/pending")
    public ResponseEntity<?> getPendingSellRequests() {
        List<BookSellRequestDTO> requests = bookSellRequestService.getPendingSellRequests();
        return ResponseEntity.ok(requests);
    }
    
    @PostMapping("/sell-requests/{id}/approve")
    public ResponseEntity<?> approveSellRequest(
            @PathVariable Long id, 
            @RequestParam Long librarianId,
            @RequestParam(required = false) String notes) {
        
        BookSellRequestDTO request = bookSellRequestService.approveSellRequest(id, librarianId, notes);
        
        if (request != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book sell request approved");
            response.put("request", request);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to approve book sell request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/sell-requests/{id}/reject")
    public ResponseEntity<?> rejectSellRequest(
            @PathVariable Long id, 
            @RequestParam Long librarianId,
            @RequestParam(required = false) String notes) {
        
        BookSellRequestDTO request = bookSellRequestService.rejectSellRequest(id, librarianId, notes);
        
        if (request != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book sell request rejected");
            response.put("request", request);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to reject book sell request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/book-orders")
    public ResponseEntity<?> getAllBookOrders() {
        List<BookOrderDTO> orders = bookOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/book-orders")
    public ResponseEntity<?> createBookOrder(@Valid @RequestBody BookOrderDTO orderDTO) {
        BookOrderDTO savedOrder = bookOrderService.createOrder(orderDTO);
        
        if (savedOrder != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to create book order");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PutMapping("/book-orders/{id}/status")
    public ResponseEntity<?> updateBookOrderStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        
        BookOrderDTO updatedOrder = bookOrderService.updateOrderStatus(id, status);
        
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to update book order status");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/earnings")
    public ResponseEntity<?> getEarnings(
            @RequestParam(required = false) Integer year) {
        
        int requestedYear = year != null ? year : Year.now().getValue();
        
        Map<String, Object> earningsData = new HashMap<>();
        
        // Get annual earnings for the requested year
        Integer annualEarnings = librarianService.getAnnualEarnings(requestedYear);
        earningsData.put("year", requestedYear);
        earningsData.put("annualEarnings", annualEarnings != null ? annualEarnings : 0);
        
        // Get monthly earnings for the requested year
        Map<Month, Integer> monthlyEarnings = librarianService.getMonthlyEarnings(requestedYear);
        earningsData.put("monthlyEarnings", monthlyEarnings);
        
        return ResponseEntity.ok(earningsData);
    }
} 