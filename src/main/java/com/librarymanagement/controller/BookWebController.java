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

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.User;
import com.librarymanagement.service.BookService;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.UserRepository;
import com.librarymanagement.service.WishlistService;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Controller
@RequestMapping("/books/view")  // Keep your original mapping
public class BookWebController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WishlistService wishlistService;
    
    @GetMapping
    public String listBooks(Model model) {
        try {
            model.addAttribute("books", bookService.getAllBooks());
        } catch (Exception e) {
            model.addAttribute("books", Collections.emptyList());
        }
        return "books/list";
    }
    
    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("book", bookService.getBook(id));
        } catch (Exception e) {
            // Handle error
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
            
            // Get user and book
            User user = userRepository.findById(userDTO.getId()).orElse(null);
            Book book = bookRepository.findById(bookId).orElse(null);
            
            if (user == null || book == null) {
                redirectAttributes.addFlashAttribute("error", "User or book not found");
                return "redirect:/books/view/" + bookId;
            }
            
            // Check if already in wishlist
            boolean alreadyInWishlist = wishlistService.isBookInWishlist(user, book);
            
            if (alreadyInWishlist) {
                redirectAttributes.addFlashAttribute("message", "This book is already in your wishlist");
            } else {
                // Add to wishlist
                wishlistService.addToWishlist(user, book);
                redirectAttributes.addFlashAttribute("message", "Book added to your wishlist successfully");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add book to wishlist: " + e.getMessage());
        }
        
        return "redirect:/books/view/" + bookId;
    }
}