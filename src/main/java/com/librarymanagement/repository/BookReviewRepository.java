package com.librarymanagement.repository;

import com.librarymanagement.entity.BookReview;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Pagination for recent reviews
    @Query("SELECT r FROM BookReview r ORDER BY r.reviewDate DESC")
    Page<BookReview> findRecentReviews(Pageable pageable);

    // Filter reviews by rating
    @Query("SELECT r FROM BookReview r WHERE r.rating >= :rating ORDER BY r.reviewDate DESC")
    List<BookReview> findReviewsByRatingAbove(Double rating);

    // Count reviews by book
    @Query("SELECT COUNT(r) FROM BookReview r WHERE r.book.id = :bookId")
    Long countReviewsByBookId(Long bookId);
}
