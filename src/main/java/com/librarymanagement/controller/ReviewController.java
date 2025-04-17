package com.librarymanagement.controller;

import com.librarymanagement.dto.ReviewDTO;
import com.librarymanagement.entity.Review;
import com.librarymanagement.entity.User;
import com.librarymanagement.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Library reviews - for all users to see
    @GetMapping
    public String libraryReviews(Model model, HttpSession session) {
        List<ReviewDTO> allReviews = reviewService.getAllReviews();
        double averageRating = reviewService.calculateAverageRating();
        
        model.addAttribute("reviews", allReviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("newReview", new ReviewDTO());
        
        // Add user info if logged in
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
        }
        
        return "reviews/library-reviews";
    }
    
    // Submit new review
    @PostMapping("/submit")
    public String submitReview(@ModelAttribute ReviewDTO reviewDTO, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to submit a review");
            return "redirect:/auth/login";
        }
        
        try {
            reviewDTO.setUserId(loggedInUser.getId());
            reviewDTO.setUsername(loggedInUser.getUsername());
            reviewService.saveReview(reviewDTO);
            redirectAttributes.addFlashAttribute("message", "Thank you for your review!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting review: " + e.getMessage());
        }
        
        return "redirect:/reviews";
    }
    
    // View user's reviews
    @GetMapping("/my-reviews")
    public String userReviews(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to view your reviews");
            return "redirect:/auth/login";
        }
        
        List<ReviewDTO> userReviews = reviewService.getReviewsByUserId(loggedInUser.getId());
        model.addAttribute("userReviews", userReviews);
        model.addAttribute("user", loggedInUser);
        
        return "reviews/user-reviews";
    }
    
    // Update review
    @PostMapping("/update/{id}")
    public String updateReview(@PathVariable Long id,
                              @ModelAttribute ReviewDTO reviewDTO,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to update a review");
            return "redirect:/auth/login";
        }
        
        try {
            // Verify the review belongs to the user
            ReviewDTO existingReview = reviewService.getReviewById(id);
            if (existingReview == null || !existingReview.getUserId().equals(loggedInUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only edit your own reviews");
                return "redirect:/reviews/my-reviews";
            }
            
            reviewDTO.setId(id);
            reviewDTO.setUserId(loggedInUser.getId());
            reviewDTO.setUsername(loggedInUser.getUsername());
            
            reviewService.updateReview(reviewDTO);
            redirectAttributes.addFlashAttribute("message", "Review updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating review: " + e.getMessage());
        }
        
        return "redirect:/reviews/my-reviews";
    }
    
    // Delete review
    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to delete a review");
            return "redirect:/auth/login";
        }
        
        try {
            // Verify the review belongs to the user
            ReviewDTO existingReview = reviewService.getReviewById(id);
            if (existingReview == null || !existingReview.getUserId().equals(loggedInUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only delete your own reviews");
                return "redirect:/reviews/my-reviews";
            }
            
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("message", "Review deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting review: " + e.getMessage());
        }
        
        return "redirect:/reviews/my-reviews";
    }
}