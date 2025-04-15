package com.librarymanagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.librarymanagement.entity.Review;
import com.librarymanagement.repository.ReviewRepository;

@Service
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    // Add a review with a rating
    public String addReview(Long userId, Long bookId, String reviewText, int rating) {
        try {
            Review review = new Review();
            review.setUserId(userId);
            review.setBookId(bookId);
            review.setReviewText(reviewText);
            review.setRating(rating); // Set the rating

            // Save the review to the database
            reviewRepository.save(review);

            return "Review added successfully!";
        } catch (DataAccessException e) {
            logger.error("Database error while adding review: {}", e.getMessage(), e);
            return "Database error: Unable to save review.";
        } catch (Exception e) {
            logger.error("Unexpected error while adding review: {}", e.getMessage(), e);
            return "An unexpected error occurred while adding the review.";
        }
    }

    // Get reviews by bookId
    public List<Review> getReviewsByBookId(Long bookId) {
        try {
            return reviewRepository.findByBookId(bookId);
        } catch (Exception e) {
            logger.error("Error fetching reviews by bookId: {}", e.getMessage(), e);
            return null;
        }
    }

    // Get reviews by userId
    public List<Review> getReviewsByUserId(Long userId) {
        try {
            return reviewRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error fetching reviews by userId: {}", e.getMessage(), e);
            return null;
        }
    }

    // Submit a review (allows the user to provide a rating)
    public String submitReview(Long userId, Long bookId, String reviewText, int rating) {
        return addReview(userId, bookId, reviewText, rating);
    }

    // Default submitReview with rating 5 if no rating is provided (for backwards compatibility)
    public String submitReview(Long userId, Long bookId, String reviewText) {
        return addReview(userId, bookId, reviewText, 5);  // Default rating is 5
    }
}
