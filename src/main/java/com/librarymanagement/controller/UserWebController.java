package com.librarymanagement.controller;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.dto.BookTransactionDTO;
import com.librarymanagement.dto.LibraryReviewDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.BookReviewService;
import com.librarymanagement.service.BookService;
import com.librarymanagement.service.BookTransactionService;
import com.librarymanagement.service.UserService;
import com.librarymanagement.service.WishlistService;
import com.librarymanagement.service.LibraryReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserWebController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookTransactionService bookTransactionService;
    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private BookReviewService bookReviewService;
    
    @Autowired
    private LibraryReviewService libraryReviewService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Get current user from session
        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        
        // Check if user is logged in
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to access your dashboard");
            return "redirect:/auth/login";
        }
        
        // Add user data to model
        model.addAttribute("user", user);
        
        // Get active borrowed books
        List<BookTransactionDTO> borrowedBooks = bookTransactionService.getCurrentBorrows(user.getId());
        model.addAttribute("borrowedBooks", borrowedBooks);
        model.addAttribute("currentBorrows", borrowedBooks);
        model.addAttribute("allBorrows", bookTransactionService.getTransactionsByUser(user.getId()));
        
        // Get user's recent transaction if any
        BookTransactionDTO recentTransaction = bookTransactionService.getRecentTransaction(user.getId());
        model.addAttribute("recentBookTransaction", recentTransaction);
        
        // Get user's wishlist
        List<BookDTO> wishlist = wishlistService.getUserWishlist(user.getId());
        model.addAttribute("wishlist", wishlist);
        
        // Get user's reviews
        model.addAttribute("myReviews", bookReviewService.getUserReviews(user.getId()));
        
        // Get user's library reviews
        model.addAttribute("libraryReviews", libraryReviewService.getUserReviews(user.getId()));
        
        // Get transaction history
        model.addAttribute("transactions", bookTransactionService.getTransactionsByUser(user.getId()));
        
        // Get top books for recommendations (ensure it's never null)
        List<BookDTO> topBooks = bookService.getTopBooks(4);
        if (topBooks == null) {
            topBooks = new ArrayList<>();
        }
        model.addAttribute("topBooks", topBooks);
        
        // Add user's coins
        model.addAttribute("userCoins", userService.getUserCoins(user.getId()));
        
        // Add empty sell requests list to avoid null pointer exception
        model.addAttribute("sellRequests", new ArrayList<>());
        
        return "users/dashboard";
    }
    
    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam Long id,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String address,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user from session
            UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
            
            // Security check - only update own profile
            if (sessionUser == null || !sessionUser.getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access");
                return "redirect:/auth/login";
            }
            
            // Debug print
            System.out.println("Update Profile Request: Phone Number = [" + phoneNumber + "]");
            
            // Create updated user object
            UserDTO updatedUser = new UserDTO();
            updatedUser.setId(id);
            updatedUser.setFullName(fullName);
            updatedUser.setEmail(email);
            
            // Handle phone number specifically - empty string is valid
            updatedUser.setPhoneNumber(phoneNumber);
            
            updatedUser.setAddress(address);
            
            // Update user in database
            UserDTO result = userService.updateUser(id, updatedUser);
            
            if (result != null) {
                // Debug print
                System.out.println("Updated User: Phone Number = [" + result.getPhoneNumber() + "]");
                
                // Update session with new user data
                session.setAttribute("currentUser", result);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to update profile");
            }
        } catch (Exception e) {
            // Print the exception for debugging
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        }
        
        return "redirect:/users/dashboard";
    }
    
    @PostMapping("/{userId}/books/{bookId}/borrow")
    public String borrowBook(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Get current user from session
        UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
        
        // Security check - only borrow books for own account
        if (sessionUser == null || !sessionUser.getId().equals(userId)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/auth/login";
        }
        
        // Create transaction DTO
        BookTransactionDTO transactionDTO = new BookTransactionDTO();
        transactionDTO.setBookId(bookId);
        transactionDTO.setUserId(userId);
        transactionDTO.setType("BORROW");
        
        // Process borrow request
        BookTransactionDTO result = bookTransactionService.createTransaction(transactionDTO);
        
        if (result != null) {
            redirectAttributes.addFlashAttribute("message", "Book borrowed successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to borrow book");
        }
        
        return "redirect:/users/dashboard";
    }
    
    @PostMapping("/{userId}/books/{bookId}/return")
    public String returnBook(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam Long transactionId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Get current user from session
        UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
        
        // Security check - only return books for own account
        if (sessionUser == null || !sessionUser.getId().equals(userId)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/auth/login";
        }
        
        // Process return request
        BookTransactionDTO result = bookTransactionService.returnBook(transactionId);
        
        if (result != null) {
            redirectAttributes.addFlashAttribute("message", "Book returned successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to return book");
        }
        
        return "redirect:/users/dashboard";
    }
    
    @PostMapping("/{userId}/books/{bookId}/wishlist/add")
    public String addToWishlist(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Get current user from session
        UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
        
        // Security check - only modify own wishlist
        if (sessionUser == null || !sessionUser.getId().equals(userId)) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/auth/login";
        }
        
        // Process wishlist add request
        boolean added = wishlistService.addToWishlist(userId, bookId);
        
        if (added) {
            redirectAttributes.addFlashAttribute("message", "Book added to wishlist successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to add book to wishlist");
        }
        
        return "redirect:/users/dashboard";
    }
    
    @PostMapping("/{userId}/books/{bookId}/wishlist/remove")
    public String removeFromWishlist(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user from session
            UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
            
            // Security check - only modify own wishlist
            if (sessionUser == null || !sessionUser.getId().equals(userId)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access");
                return "redirect:/auth/login";
            }
            
            // Process wishlist remove request
            boolean removed = wishlistService.removeFromWishlist(userId, bookId);
            
            if (removed) {
                redirectAttributes.addFlashAttribute("message", "Book removed from wishlist successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Book not found in your wishlist");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error removing book from wishlist: " + e.getMessage());
        }
        
        return "redirect:/users/dashboard";
    }
    
    @PostMapping("/{userId}/books/{bookId}/wishlist")
    public String processWishlistAction(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam(name = "_method", required = false) String method,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        if ("DELETE".equalsIgnoreCase(method)) {
            // Call the existing remove method
            return removeFromWishlist(userId, bookId, session, redirectAttributes);
        }
        
        // Default fallback - could be extended for other methods
        return "redirect:/users/dashboard";
    }
    
    @PostMapping("/{userId}/books/{bookId}/purchase")
    public String purchaseBook(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam int quantity,
            @RequestParam(defaultValue = "0") int useCoins,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user from session
            UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
            
            // Security check - only purchase books for own account
            if (sessionUser == null || !sessionUser.getId().equals(userId)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access");
                return "redirect:/auth/login";
            }
            
            // Validate quantity
            if (quantity < 1 || quantity > 3) {
                redirectAttributes.addFlashAttribute("error", "Invalid quantity. Must be between 1 and 3");
                return "redirect:/users/dashboard";
            }
            
            // Validate coin usage
            if (useCoins < 0 || useCoins > sessionUser.getCoins()) {
                redirectAttributes.addFlashAttribute("error", "Invalid coin usage");
                return "redirect:/users/dashboard";
            }
            
            // Get book details
            BookDTO book = bookService.getBook(bookId);
            if (book == null) {
                redirectAttributes.addFlashAttribute("error", "Book not found");
                return "redirect:/users/dashboard";
            }
            
            // Check if enough stock
            if (book.getStock() < quantity) {
                redirectAttributes.addFlashAttribute("error", "Not enough stock available");
                return "redirect:/users/dashboard";
            }
            
            // Create purchase transactions
            boolean success = false;
            for (int i = 0; i < quantity; i++) {
                BookTransactionDTO transactionDTO = new BookTransactionDTO();
                transactionDTO.setBookId(bookId);
                transactionDTO.setUserId(userId);
                transactionDTO.setType("PURCHASE");
                
                // Apply coins to only the first transaction
                if (i == 0 && useCoins > 0) {
                    transactionDTO.setCoinsUsed(useCoins);
                }
                
                BookTransactionDTO result = bookTransactionService.createTransaction(transactionDTO);
                if (result != null) {
                    success = true;
                }
            }
            
            if (success) {
                redirectAttributes.addFlashAttribute("message", quantity + " copy/copies of " + book.getTitle() + " purchased successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to purchase book");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error purchasing book: " + e.getMessage());
        }
        
        return "redirect:/users/dashboard";
    }
    
    @PostMapping("/{userId}/library-review")
    public String submitLibraryReview(
            @PathVariable Long userId,
            @RequestParam int rating,
            @RequestParam String title,
            @RequestParam String reviewText,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user from session
            UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
            
            // Security check - only submit reviews for own account
            if (sessionUser == null || !sessionUser.getId().equals(userId)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access");
                return "redirect:/auth/login";
            }
            
            // Validate rating
            if (rating < 1 || rating > 5) {
                redirectAttributes.addFlashAttribute("error", "Invalid rating. Must be between 1 and 5");
                return "redirect:/users/dashboard";
            }
            
            // Create review
            LibraryReviewDTO reviewDTO = new LibraryReviewDTO();
            reviewDTO.setUserId(userId);
            reviewDTO.setRating(rating);
            reviewDTO.setTitle(title);
            reviewDTO.setReviewText(reviewText);
            
            LibraryReviewDTO result = libraryReviewService.createReview(reviewDTO);
            
            if (result != null) {
                redirectAttributes.addFlashAttribute("message", "Library review submitted successfully");
                // Add coins as reward for review
                userService.addCoins(userId, 5);
                redirectAttributes.addFlashAttribute("message", "Library review submitted successfully. You earned 5 coins!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to submit library review");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting library review: " + e.getMessage());
        }
        
        return "redirect:/users/dashboard";
    }
    
    @PostMapping("/{userId}/sell-book")
    public String donateBook(
            @PathVariable Long userId,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String description,
            @RequestParam String condition,
            @RequestParam double askingPrice,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user from session
            UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
            
            // Security check - only donate books from own account
            if (sessionUser == null || !sessionUser.getId().equals(userId)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access");
                return "redirect:/auth/login";
            }
            
            // Create book for donation
            BookDTO bookDTO = new BookDTO();
            bookDTO.setTitle(title);
            bookDTO.setAuthor(author);
            
            if (isbn != null && !isbn.trim().isEmpty()) {
                bookDTO.setIsbn(isbn);
            }
            
            if (description != null && !description.trim().isEmpty()) {
                bookDTO.setDescription(description);
            }
            
            // Setting category as condition since BookDTO doesn't have condition field
            bookDTO.setCategory(condition);
            
            // Set price to 0 for donations
            bookDTO.setPrice(new BigDecimal(0));
            
            // Set default stock
            bookDTO.setStock(1);
            
            // Save the book donation
            String result = bookService.addBook(bookDTO);
            
            if (result != null && result.contains("success")) {
                // Add coins as reward for donating a book
                userService.addCoins(userId, 5); // More coins for donations
                redirectAttributes.addFlashAttribute("message", "Thank you for your book donation! You earned 5 coins as a thank you gift.");
            } else {
                redirectAttributes.addFlashAttribute("error", result != null ? result : "Failed to submit book donation");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error submitting book donation: " + e.getMessage());
        }
        
        return "redirect:/users/dashboard";
    }

    @PostMapping("/buyBook")
    public String buyBook(@RequestParam("bookId") Long bookId, 
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to purchase a book");
            return "redirect:/auth/login";
        }
        
        try {
            BookTransactionDTO transaction = bookTransactionService.purchaseBook(user.getId(), bookId);
            redirectAttributes.addFlashAttribute("success", "Book purchased successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while purchasing the book. Please try again.");
        }
        
        return "redirect:/books/detail?id=" + bookId;
    }
}