package com.librarymanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.librarymanagement.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Custom query method to find reviews by bookId
    List<Review> findByBookId(Long bookId);

    // Custom query method to find reviews by userId
    List<Review> findByUserId(Long userId);
}
