package com.librarymanagement.service;

import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    public String addReview(Long userId, Long bookId, String review) {
        // Business logic to add review
        return "Review added successfully!";
    }
}
