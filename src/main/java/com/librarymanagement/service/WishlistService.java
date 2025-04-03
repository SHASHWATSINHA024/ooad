package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.WishlistItem;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.UserRepository;
import com.librarymanagement.repository.WishlistItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BookService bookService;
    
    /**
     * Add a book to user's wishlist
     */
    public boolean addToWishlist(Long userId, Long bookId) {
        // Check if the book is already in the wishlist
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        Book book = bookOpt.get();
        
        // Check if already in wishlist
        if (wishlistItemRepository.existsByUserAndBook(user, book)) {
            return true; // Already in wishlist, consider it a success
        }
        
        // Add to wishlist
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setUser(user);
        wishlistItem.setBook(book);
        
        wishlistItemRepository.save(wishlistItem);
        return true;
    }
    
    /**
     * Remove a book from user's wishlist
     */
    public boolean removeFromWishlist(Long userId, Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        Book book = bookOpt.get();
        
        // Check if in wishlist
        Optional<WishlistItem> wishlistItemOpt = wishlistItemRepository.findByUserAndBook(user, book);
        
        if (wishlistItemOpt.isEmpty()) {
            return false; // Not in wishlist
        }
        
        // Remove from wishlist
        wishlistItemRepository.delete(wishlistItemOpt.get());
        return true;
    }
    
    /**
     * Get all books in user's wishlist
     */
    public List<BookDTO> getUserWishlist(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }
        
        User user = userOpt.get();
        List<WishlistItem> wishlistItems = wishlistItemRepository.findByUser(user);
        List<BookDTO> books = new ArrayList<>();
        
        for (WishlistItem item : wishlistItems) {
            BookDTO bookDTO = bookService.getBook(item.getBook().getId());
            if (bookDTO != null) {
                books.add(bookDTO);
            }
        }
        
        return books;
    }
    
    /**
     * Check if a book is in user's wishlist
     */
    public boolean isInWishlist(Long userId, Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        Book book = bookOpt.get();
        
        return wishlistItemRepository.existsByUserAndBook(user, book);
    }
} 