package com.librarymanagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.LibraryReviewDTO;
import com.librarymanagement.entity.LibraryReview;
import com.librarymanagement.entity.User;
import com.librarymanagement.repository.LibraryReviewRepository;
import com.librarymanagement.repository.UserRepository;

@Service
public class LibraryReviewService {

    @Autowired
    private LibraryReviewRepository libraryReviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Create a new library review
     */
    public LibraryReviewDTO createReview(LibraryReviewDTO reviewDTO) {
        Optional<User> userOpt = userRepository.findById(reviewDTO.getUserId());
        
        if (userOpt.isEmpty()) {
            return null;
        }
        
        User user = userOpt.get();
        
        // Create and save the review entity
        LibraryReview review = new LibraryReview();
        review.setUser(user);
        review.setTitle(reviewDTO.getTitle());
        review.setRating(reviewDTO.getRating());
        review.setReviewText(reviewDTO.getReviewText());
        review.setReviewDate(LocalDateTime.now());
        
        LibraryReview savedReview = libraryReviewRepository.save(review);
        
        // Convert back to DTO
        LibraryReviewDTO savedReviewDTO = new LibraryReviewDTO();
        savedReviewDTO.setId(savedReview.getId());
        savedReviewDTO.setUserId(savedReview.getUser().getId());
        savedReviewDTO.setUserName(savedReview.getUser().getUsername());
        savedReviewDTO.setTitle(savedReview.getTitle());
        savedReviewDTO.setRating(savedReview.getRating());
        savedReviewDTO.setReviewText(savedReview.getReviewText());
        savedReviewDTO.setReviewDate(savedReview.getReviewDate());
        
        return savedReviewDTO;
    }
    
    /**
     * Get all reviews for a user
     */
    public List<LibraryReviewDTO> getUserReviews(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }
        
        User user = userOpt.get();
        List<LibraryReview> reviews = libraryReviewRepository.findByUser(user);
        
        List<LibraryReviewDTO> reviewDTOs = new ArrayList<>();
        for (LibraryReview review : reviews) {
            LibraryReviewDTO dto = new LibraryReviewDTO();
            dto.setId(review.getId());
            dto.setUserId(review.getUser().getId());
            dto.setUserName(review.getUser().getUsername());
            dto.setTitle(review.getTitle());
            dto.setRating(review.getRating());
            dto.setReviewText(review.getReviewText());
            dto.setReviewDate(review.getReviewDate());
            
            reviewDTOs.add(dto);
        }
        
        return reviewDTOs;
    }
    
    /**
     * Get all recent library reviews
     */
    public List<LibraryReviewDTO> getRecentReviews(int limit) {
        List<LibraryReview> reviews = libraryReviewRepository.findRecentReviews();
        
        List<LibraryReviewDTO> reviewDTOs = new ArrayList<>();
        int count = 0;
        for (LibraryReview review : reviews) {
            if (count >= limit) {
                break;
            }
            
            LibraryReviewDTO dto = new LibraryReviewDTO();
            dto.setId(review.getId());
            dto.setUserId(review.getUser().getId());
            dto.setUserName(review.getUser().getUsername());
            dto.setTitle(review.getTitle());
            dto.setRating(review.getRating());
            dto.setReviewText(review.getReviewText());
            dto.setReviewDate(review.getReviewDate());
            
            reviewDTOs.add(dto);
            count++;
        }
        
        return reviewDTOs;
    }
    
    /**
     * Get average library rating
     */
    public Double getAverageRating() {
        return libraryReviewRepository.findAverageRating();
    }
} 