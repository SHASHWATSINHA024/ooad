package com.librarymanagement.repository;

import com.librarymanagement.entity.BookReview;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findByBook(Book book);
    List<BookReview> findByUser(User user);
    
    @Query("SELECT AVG(r.rating) FROM BookReview r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(Long bookId);
    
    @Query("SELECT r FROM BookReview r ORDER BY r.reviewDate DESC")
    List<BookReview> findRecentReviews();
} 