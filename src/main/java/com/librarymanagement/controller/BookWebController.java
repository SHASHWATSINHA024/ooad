package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.dto.BookReviewDTO;
import com.librarymanagement.dto.BookTransactionDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.User;
import com.librarymanagement.service.BookReviewService;
import com.librarymanagement.service.BookService;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.UserRepository;
import com.librarymanagement.service.BookTransactionService;
import com.librarymanagement.service.WishlistService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/books")  // Change from "/books/view" to "/books"
public class BookWebController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private BookTransactionService bookTransactionService;
    
    @Autowired
    private BookReviewService bookReviewService;
    
    @GetMapping("/view")
    public String listBooks(Model model) {
        try {
            model.addAttribute("books", bookService.getAllBooks());
        } catch (Exception e) {
            model.addAttribute("books", Collections.emptyList());
        }
        return "books/list";
    }
    
    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            BookDTO book = bookService.getBook(id);
            if (book == null) {
                redirectAttributes.addFlashAttribute("error", "Book not found");
                return "redirect:/books/view";
            }
            model.addAttribute("book", book);
            
            // Add book reviews to the model
            List<BookReviewDTO> reviews = bookReviewService.getBookReviews(id);
            model.addAttribute("reviews", reviews);
            
            // Get average rating
            Double avgRating = bookReviewService.getAverageRating(id);
            model.addAttribute("averageRating", avgRating);
            
        } catch (Exception e) {
            model.addAttribute("error", "Error retrieving book: " + e.getMessage());
            return "books/detail"; // Still return the detail view with an error message
        }
        return "books/detail";
    }
    
    // Add this method for wishlist functionality
    @PostMapping("/addToWishlist")
    public String addToWishlist(@RequestParam("bookId") Long bookId, 
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            // Get current user from session
            UserDTO userDTO = (UserDTO) session.getAttribute("currentUser");
            
            if (userDTO == null) {
                redirectAttributes.addFlashAttribute("error", "Please log in to add books to your wishlist");
                return "redirect:/auth/login";
            }
            
            // Process wishlist add request using the service method with IDs
            boolean added = wishlistService.addToWishlist(userDTO.getId(), bookId);
            
            if (added) {
                redirectAttributes.addFlashAttribute("message", "Book added to your wishlist successfully");
            } else {
                redirectAttributes.addFlashAttribute("message", "This book is already in your wishlist");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add book to wishlist: " + e.getMessage());
        }
        
        return "redirect:/books/view/" + bookId;
    }
    
    // Add this method to handle book borrowing
    @PostMapping("/borrow")
    public String borrowBook(
            @RequestParam("bookId") Long bookId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user from session
            UserDTO userDTO = (UserDTO) session.getAttribute("currentUser");
            
            if (userDTO == null) {
                redirectAttributes.addFlashAttribute("error", "Please log in to borrow books");
                return "redirect:/auth/login";
            }
            
            // Check if user has already borrowed this book and not returned it
            List<BookTransactionDTO> currentBorrows = bookTransactionService.getCurrentBorrows(userDTO.getId());
            boolean alreadyBorrowed = currentBorrows.stream()
                    .anyMatch(borrow -> borrow.getBookId().equals(bookId) && borrow.getReturnDate() == null);
            
            if (alreadyBorrowed) {
                redirectAttributes.addFlashAttribute("error", "You have already borrowed this book and not returned it yet");
                return "redirect:/books/view/" + bookId;
            }
            
            // Create transaction DTO
            BookTransactionDTO transactionDTO = new BookTransactionDTO();
            transactionDTO.setBookId(bookId);
            transactionDTO.setUserId(userDTO.getId());
            transactionDTO.setType("BORROW");
            
            // Process borrow request using the transaction service
            BookTransactionDTO result = bookTransactionService.createTransaction(transactionDTO);
            
            if (result != null) {
                redirectAttributes.addFlashAttribute("message", "Book borrowed successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to borrow book. It may be out of stock.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error borrowing book: " + e.getMessage());
        }
        
        // Redirect back to the book detail page
        return "redirect:/books/view/" + bookId;
    }

    // Add method to handle book reviews
    @PostMapping("/addReview")
    public String addReview(
            @RequestParam("bookId") Long bookId,
            @RequestParam("rating") Integer rating,
            @RequestParam("content") String reviewText,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user from session
            UserDTO userDTO = (UserDTO) session.getAttribute("currentUser");
            
            if (userDTO == null) {
                redirectAttributes.addFlashAttribute("error", "Please log in to submit a review");
                return "redirect:/auth/login";
            }
            
            // Create review DTO
            BookReviewDTO reviewDTO = new BookReviewDTO();
            reviewDTO.setBookId(bookId);
            reviewDTO.setUserId(userDTO.getId());
            reviewDTO.setRating(rating);
            reviewDTO.setReviewText(reviewText);
            
            // Process review submission
            BookReviewDTO result = bookReviewService.addReview(reviewDTO);
            
            if (result != null) {
                redirectAttributes.addFlashAttribute("message", "Your review has been submitted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to submit review");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting review: " + e.getMessage());
        }
        
        // Redirect back to the book detail page
        return "redirect:/books/view/" + bookId;
    }
}