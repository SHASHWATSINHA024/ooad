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
        return reviewService.addReview(
            reviewDTO.getBookId(),  // ✅ Extracting Long bookId
            reviewDTO.getUserId(),  // ✅ Extracting Long userId
            reviewDTO.getReviewText() // ✅ Extracting review text
        );
    }
}
