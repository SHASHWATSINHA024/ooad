package com.librarymanagement.controller;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.BookService;
import com.librarymanagement.service.ReviewService;
import com.librarymanagement.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/books")
public class BookWebController {

    @Autowired
    private BookService bookService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ReviewService reviewService;

    // List all books
    @GetMapping
    public String listBooks(Model model) {
        try {
            model.addAttribute("books", bookService.getAllBooks());
        } catch (Exception e) {
            model.addAttribute("books", Collections.emptyList());
            System.err.println("Error fetching books: " + e.getMessage());
        }
        return "books/list";
    }

    // View details of a single book
    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            BookDTO book = bookService.getBook(id);
            if (book == null) {
                redirectAttributes.addFlashAttribute("error", "Book not found");
                return "redirect:/books";
            }

            model.addAttribute("book", book);
            model.addAttribute("reviews", reviewService.getReviewsByBookId(id));

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error retrieving book: " + e.getMessage());
            return "redirect:/books";
        }
        return "books/detail";
    }

    // Add to wishlist (AJAX version)
    @PostMapping("/wishlist")
    @ResponseBody
    public ResponseEntity<?> addToWishlistAjax(@RequestBody Map<String, Long> data, HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("currentUser");

        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Please log in."));
        }

        Long bookId = data.get("bookId");

        boolean success = wishlistService.addToWishlist(userDTO.getId(), bookId);

        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Book added to wishlist successfully."));
        } else {
            return ResponseEntity.ok(Map.of("success", false, "message", "Book is already in your wishlist."));
        }
    }

    // Submit a review
    @PostMapping("/review")
    public String submitReview(@RequestParam Long bookId, @RequestParam int rating, @RequestParam String review, RedirectAttributes redirectAttributes) {
        try {
            reviewService.addReview(bookId, bookId, review, rating); // Use correct parameters
            redirectAttributes.addFlashAttribute("message", "Review submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit the review.");
        }
        return "redirect:/books/view/" + bookId;
    }

    // Purchase a book
    @PostMapping("/buy")
    public String buyBook(@RequestParam Long bookId, RedirectAttributes redirectAttributes, HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("currentUser");

        if (userDTO == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to buy a book.");
            return "redirect:/books/view/" + bookId;
        }

        try {
            boolean success = bookService.buyBook(bookId, userDTO.getId());
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Book purchased successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Not enough stock available for purchase.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error processing your purchase: " + e.getMessage());
        }

        return "redirect:/books/view/" + bookId;
    }
}
