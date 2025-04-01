package com.librarymanagement.controller;

import com.librarymanagement.dto.ReviewDTO;
import com.librarymanagement.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public String addReview(@RequestBody ReviewDTO reviewDTO) {
        // Debugging: Print received data
        System.out.println("Book ID: " + reviewDTO.getBookId());
        System.out.println("User ID: " + reviewDTO.getUserId());
        System.out.println("Review: " + reviewDTO.getReviewText());

        return reviewService.addReview(
            reviewDTO.getBookId(),
            reviewDTO.getUserId(),
            reviewDTO.getReviewText()
        );
    }
}
