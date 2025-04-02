package com.librarymanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.entity.Review;
import com.librarymanagement.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public String addReview(Long userId, Long bookId, String reviewText) {
        // Create a new Review object from the DTO
        Review review = new Review();
        review.setUserId(userId);
        review.setBookId(bookId);
        review.setReviewText(reviewText);

        // Save the Review to the database
        reviewRepository.save(review);

        return "Review added successfully!";
    }

    // Method to retrieve all reviews by userId
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    // Method to retrieve all reviews by bookId
    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }
}
