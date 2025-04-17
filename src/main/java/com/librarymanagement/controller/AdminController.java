package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.dto.BookTransactionDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.entity.BookTransaction;
import com.librarymanagement.service.BookService;
import com.librarymanagement.service.BookTransactionService;
import com.librarymanagement.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookTransactionService bookTransactionService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // Get current admin/librarian from session
            UserDTO user = (UserDTO) session.getAttribute("currentUser");
            
            // Check if user is logged in and has admin role
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Please log in to access the admin dashboard");
                return "redirect:/auth/login";
            }
            
            if (!"ADMIN".equals(user.getRole()) && !"LIBRARIAN".equals(user.getRole())) {
                redirectAttributes.addFlashAttribute("error", "You do not have permission to access this page");
                return "redirect:/users/dashboard";
            }
            
            model.addAttribute("user", user);
            
            // Add real stats
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalBooks", bookService.getAllBooks().size());
            stats.put("totalUsers", userService.getAllUsers().size());
            
            // Count number of books sold (PURCHASE transactions)
            List<BookTransactionDTO> allTransactions = bookTransactionService.getAllTransactions();
            long booksSold = allTransactions.stream()
                .filter(t -> "PURCHASE".equals(t.getType()))
                .count();
            stats.put("booksSold", booksSold);
            
            stats.put("overdueBooks", bookTransactionService.getOverdueBooks().size());
            model.addAttribute("stats", stats);
            
            // Add real data for lists
            model.addAttribute("recentActivities", bookTransactionService.getRecentTransactions(10));
            model.addAttribute("popularBooks", bookService.getTopBooks(5));
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("allTransactions", allTransactions);
            model.addAttribute("books", bookService.getAllBooks());
            
            return "admin/dashboard";
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            
            // Add error message and redirect to login
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/auth/login";
        }
    }
    
    @GetMapping("/users")
    public String users(Model model) {
        // Add user management data
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }
    
    @GetMapping("/librarians/add")
    public String addLibrarianForm(Model model) {
        model.addAttribute("librarian", new UserDTO());
        return "admin/librarian-form";
    }
    
    @PostMapping("/librarians/add")
    public String addLibrarian(UserDTO librarian, RedirectAttributes redirectAttributes) {
        try {
            // Set role to LIBRARIAN
            librarian.setRole("LIBRARIAN");
            librarian.setStatus("ACTIVE");
            
            // Register the librarian
            UserDTO result = userService.registerUser(librarian);
            if (result != null) {
                redirectAttributes.addFlashAttribute("message", "Librarian added successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to add librarian");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding librarian: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/librarians/{id}/edit")
    public String editLibrarianForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        UserDTO librarian = userService.getUserById(id);
        if (librarian == null) {
            redirectAttributes.addFlashAttribute("error", "Librarian not found");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("librarian", librarian);
        return "admin/librarian-form";
    }
    
    @PostMapping("/librarians/{id}/edit")
    public String updateLibrarian(@PathVariable Long id, UserDTO librarian, RedirectAttributes redirectAttributes) {
        try {
            // Maintain role as LIBRARIAN
            librarian.setRole("LIBRARIAN");
            
            UserDTO result = userService.updateUser(id, librarian);
            if (result != null) {
                redirectAttributes.addFlashAttribute("message", "Librarian updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to update librarian");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating librarian: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/books")
    public String books(Model model) {
        // Add book management data
        model.addAttribute("books", bookService.getAllBooks());
        return "admin/books";
    }
    
    @GetMapping("/books/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "admin/book-form";
    }
    
    @PostMapping("/books/add")
    public String addBook(BookDTO book, RedirectAttributes redirectAttributes) {
        try {
            String result = bookService.addBook(book);
            if (result != null) {
                redirectAttributes.addFlashAttribute("message", result);
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to add book");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding book: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/books/{id}/edit")
    public String editBookForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        BookDTO book = bookService.getBook(id);
        if (book == null) {
            redirectAttributes.addFlashAttribute("error", "Book not found");
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("book", book);
        return "admin/book-form";
    }
    
    @PostMapping("/books/{id}/edit")
    public String updateBook(@PathVariable Long id, BookDTO book, RedirectAttributes redirectAttributes) {
        try {
            String result = bookService.updateBook(id, book);
            redirectAttributes.addFlashAttribute("message", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating book: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String result = bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("message", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting book: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/transactions")
    public String transactions(Model model) {
        // Add transaction management data
        model.addAttribute("transactions", bookTransactionService.getAllTransactions());
        return "admin/transactions";
    }
    
    @PostMapping("/users/{id}/approve")
    public String approveUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            if (userDTO != null) {
                userDTO.setStatus("ACTIVE");
                userService.updateUser(id, userDTO);
                redirectAttributes.addFlashAttribute("message", "User approved successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error approving user: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/users/{id}/enable")
    public String enableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            if (userDTO != null) {
                userDTO.setStatus("ACTIVE");
                userService.updateUser(id, userDTO);
                redirectAttributes.addFlashAttribute("message", "User enabled successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error enabling user: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/users/{id}/disable")
    public String disableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            UserDTO userDTO = userService.getUserById(id);
            if (userDTO != null) {
                userDTO.setStatus("SUSPENDED");
                userService.updateUser(id, userDTO);
                redirectAttributes.addFlashAttribute("message", "User disabled successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error disabling user: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}