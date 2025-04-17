package com.librarymanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.librarymanagement.entity.LibraryReview;
import com.librarymanagement.entity.User;

@Repository
public interface LibraryReviewRepository extends JpaRepository<LibraryReview, Long> {
    List<LibraryReview> findByUser(User user);
    
    @Query("SELECT lr FROM LibraryReview lr ORDER BY lr.reviewDate DESC")
    List<LibraryReview> findRecentReviews();
    
    @Query("SELECT AVG(lr.rating) FROM LibraryReview lr")
    Double findAverageRating();
} 