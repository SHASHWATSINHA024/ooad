package com.librarymanagement.service;

import com.librarymanagement.dto.ReviewDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.Review;
import com.librarymanagement.entity.User;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.ReviewRepository;
import com.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.map(this::convertToDTO).orElse(null);
    }

    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public double calculateAverageRating() {
        List<Review> allReviews = reviewRepository.findAll();
        if (allReviews.isEmpty()) {
            return 0.0;
        }
        
        double totalRating = allReviews.stream()
                .mapToInt(Review::getRating)
                .sum();
        
        return totalRating / allReviews.size();
    }

    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
        Review review = convertToEntity(reviewDTO);
        review.setCreatedAt(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    public ReviewDTO updateReview(ReviewDTO reviewDTO) {
        Optional<Review> existingReview = reviewRepository.findById(reviewDTO.getId());
        
        if (existingReview.isPresent()) {
            Review review = existingReview.get();
            review.setRating(reviewDTO.getRating());
            review.setTitle(reviewDTO.getTitle());
            review.setReviewText(reviewDTO.getReviewText());
            review.setUpdatedAt(LocalDateTime.now());
            
            Review updatedReview = reviewRepository.save(review);
            return convertToDTO(updatedReview);
        }
        
        return null;
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        
        if (review.getBook() != null) {
            dto.setBookId(review.getBook().getId());
            dto.setBookTitle(review.getBook().getTitle());
        }
        
        dto.setRating(review.getRating());
        dto.setTitle(review.getTitle());
        dto.setReviewText(review.getReviewText());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        
        return dto;
    }

    private Review convertToEntity(ReviewDTO dto) {
        Review review = new Review();
        
        if (dto.getId() != null) {
            review.setId(dto.getId());
        }
        
        // Set user
        Optional<User> user = userRepository.findById(dto.getUserId());
        user.ifPresent(review::setUser);
        
        // Set book if bookId is provided
        if (dto.getBookId() != null) {
            Optional<Book> book = bookRepository.findById(dto.getBookId());
            book.ifPresent(review::setBook);
        }
        
        review.setRating(dto.getRating());
        review.setTitle(dto.getTitle());
        review.setReviewText(dto.getReviewText());
        
        if (dto.getCreatedAt() != null) {
            review.setCreatedAt(dto.getCreatedAt());
        }
        
        if (dto.getUpdatedAt() != null) {
            review.setUpdatedAt(dto.getUpdatedAt());
        }
        
        return review;
    }

    // Legacy method for backward compatibility
    public String addReview(Long userId, Long bookId, String reviewText) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book = bookRepository.findById(bookId);
        
        if (user.isPresent() && book.isPresent()) {
            Review review = new Review();
            review.setUser(user.get());
            review.setBook(book.get());
            review.setReviewText(reviewText);
            review.setCreatedAt(LocalDateTime.now());
            reviewRepository.save(review);
            return "Review added successfully";
        }
        
        return "Failed to add review";
    }
}
