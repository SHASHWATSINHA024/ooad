package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.BookReviewDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BookReview;
import com.librarymanagement.entity.User;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BookReviewRepository;
import com.librarymanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {

    @Autowired
    private BookReviewRepository bookReviewRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Add a new book review
     */
    public BookReviewDTO addReview(BookReviewDTO reviewDTO) {
        Optional<Book> bookOpt = bookRepository.findById(reviewDTO.getBookId());
        Optional<User> userOpt = userRepository.findById(reviewDTO.getUserId());
        
        if (bookOpt.isEmpty() || userOpt.isEmpty()) {
            return null;
        }
        
        Book book = bookOpt.get();
        User user = userOpt.get();
        
        BookReview review = new BookReview();
        review.setBook(book);
        review.setUser(user);
        review.setRating(reviewDTO.getRating());
        review.setReviewText(reviewDTO.getReviewText());
        review.setReviewDate(LocalDateTime.now());
        
        BookReview savedReview = bookReviewRepository.save(review);
        
        BookReviewDTO savedReviewDTO = new BookReviewDTO();
        savedReviewDTO.setId(savedReview.getId());
        savedReviewDTO.setBookId(savedReview.getBook().getId());
        savedReviewDTO.setBookTitle(savedReview.getBook().getTitle());
        savedReviewDTO.setUserId(savedReview.getUser().getId());
        savedReviewDTO.setUserName(savedReview.getUser().getUsername());
        savedReviewDTO.setRating(savedReview.getRating());
        savedReviewDTO.setReviewText(savedReview.getReviewText());
        savedReviewDTO.setReviewDate(savedReview.getReviewDate());
        
        return savedReviewDTO;
    }
    
    /**
     * Get reviews for a book
     */
    public List<BookReviewDTO> getBookReviews(Long bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (bookOpt.isEmpty()) {
            return new ArrayList<>();
        }
        
        Book book = bookOpt.get();
        List<BookReview> reviews = bookReviewRepository.findByBook(book);
        List<BookReviewDTO> reviewDTOs = new ArrayList<>();
        
        for (BookReview review : reviews) {
            BookReviewDTO reviewDTO = new BookReviewDTO();
            reviewDTO.setId(review.getId());
            reviewDTO.setBookId(review.getBook().getId());
            reviewDTO.setBookTitle(review.getBook().getTitle());
            reviewDTO.setUserId(review.getUser().getId());
            reviewDTO.setUserName(review.getUser().getUsername());
            reviewDTO.setRating(review.getRating());
            reviewDTO.setReviewText(review.getReviewText());
            reviewDTO.setReviewDate(review.getReviewDate());
            
            reviewDTOs.add(reviewDTO);
        }
        
        return reviewDTOs;
    }
    
    /**
     * Get reviews by a user
     */
    public List<BookReviewDTO> getUserReviews(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }
        
        User user = userOpt.get();
        List<BookReview> reviews = bookReviewRepository.findByUser(user);
        List<BookReviewDTO> reviewDTOs = new ArrayList<>();
        
        for (BookReview review : reviews) {
            BookReviewDTO reviewDTO = new BookReviewDTO();
            reviewDTO.setId(review.getId());
            reviewDTO.setBookId(review.getBook().getId());
            reviewDTO.setBookTitle(review.getBook().getTitle());
            reviewDTO.setUserId(review.getUser().getId());
            reviewDTO.setUserName(review.getUser().getUsername());
            reviewDTO.setRating(review.getRating());
            reviewDTO.setReviewText(review.getReviewText());
            reviewDTO.setReviewDate(review.getReviewDate());
            
            reviewDTOs.add(reviewDTO);
        }
        
        return reviewDTOs;
    }
    
    /**
     * Get recent reviews
     */
    public List<BookReviewDTO> getRecentReviews(int limit) {
        List<BookReview> reviews = bookReviewRepository.findRecentReviews();
        List<BookReviewDTO> reviewDTOs = new ArrayList<>();
        
        int count = 0;
        for (BookReview review : reviews) {
            if (count >= limit) {
                break;
            }
            
            BookReviewDTO reviewDTO = new BookReviewDTO();
            reviewDTO.setId(review.getId());
            reviewDTO.setBookId(review.getBook().getId());
            reviewDTO.setBookTitle(review.getBook().getTitle());
            reviewDTO.setUserId(review.getUser().getId());
            reviewDTO.setUserName(review.getUser().getUsername());
            reviewDTO.setRating(review.getRating());
            reviewDTO.setReviewText(review.getReviewText());
            reviewDTO.setReviewDate(review.getReviewDate());
            
            reviewDTOs.add(reviewDTO);
            count++;
        }
        
        return reviewDTOs;
    }
    
    /**
     * Get average rating for a book
     */
    public Double getAverageRating(Long bookId) {
        return bookReviewRepository.findAverageRatingByBookId(bookId);
    }
} 