package com.librarymanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.librarymanagement.dto.LibraryReviewDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.LibraryReviewService;
import com.librarymanagement.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/reviews")
public class LibraryReviewController {

    @Autowired
    private LibraryReviewService libraryReviewService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Display the library reviews page
     */
    @GetMapping("/library")
    public String showLibraryReviews(Model model, HttpSession session) {
        try {
            // Get recent reviews
            List<LibraryReviewDTO> recentReviews = libraryReviewService.getRecentReviews(10);
            
            // Get average rating
            Double averageRating = libraryReviewService.getAverageRating();
            
            // Add current user if logged in
            UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
            if (currentUser != null) {
                model.addAttribute("user", currentUser);
            }
            
            model.addAttribute("reviews", recentReviews);
            model.addAttribute("averageRating", averageRating != null ? averageRating : 0.0);
            model.addAttribute("message", model.asMap().get("message"));
            model.addAttribute("error", model.asMap().get("error"));
            
            return "reviews/library-reviews";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading reviews: " + e.getMessage());
            return "error/general";
        }
    }
    
    /**
     * Process a new library review submission
     */
    @PostMapping("/add")
    public String addLibraryReview(
            @RequestParam("rating") int rating,
            @RequestParam("title") String title,
            @RequestParam("reviewText") String reviewText,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Get current user from session
        UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
        
        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to submit a review");
            return "redirect:/auth/login";
        }
        
        // Validate rating
        if (rating < 1 || rating > 5) {
            redirectAttributes.addFlashAttribute("error", "Invalid rating. Must be between 1 and 5");
            return "redirect:/reviews/library";
        }
        
        try {
            // Create review DTO
            LibraryReviewDTO reviewDTO = new LibraryReviewDTO();
            reviewDTO.setUserId(sessionUser.getId());
            reviewDTO.setRating(rating);
            reviewDTO.setTitle(title);
            reviewDTO.setReviewText(reviewText);
            
            // Save the review
            LibraryReviewDTO result = libraryReviewService.createReview(reviewDTO);
            
            if (result != null) {
                // Add coins as reward for review
                userService.addCoins(sessionUser.getId(), 5);
                redirectAttributes.addFlashAttribute("message", "Library review submitted successfully. You earned 5 coins!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to submit library review");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error submitting review: " + e.getMessage());
        }
        
        return "redirect:/reviews/library";
    }
    
    /**
     * Get library reviews for a specific user (for profile viewing)
     */
    @GetMapping("/user/{userId}")
    public String getUserReviews(@PathVariable Long userId, Model model) {
        List<LibraryReviewDTO> userReviews = libraryReviewService.getUserReviews(userId);
        model.addAttribute("userReviews", userReviews);
        
        return "reviews/user-reviews";
    }
} 