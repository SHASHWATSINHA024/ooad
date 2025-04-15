package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.UserRepository;
import com.librarymanagement.repository.WishlistItemRepository;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.WishlistItem;
import com.librarymanagement.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

@Service
public class WishlistService {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Add a book to a user's wishlist (using User and Book objects)
     */
    public WishlistItem addToWishlist(User user, Book book) {
        // Check if already in wishlist
        if (wishlistItemRepository.existsByUserAndBook(user, book)) {
            System.out.println("Book is already in the wishlist");
            return null; // Already in wishlist
        }

        // Create wishlist item
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setUser(user);
        wishlistItem.setBook(book);
        wishlistItem.setAddedAt(LocalDateTime.now());

        return wishlistItemRepository.save(wishlistItem);
    }

    /**
     * Add a book to a user's wishlist (using IDs)
     */
    public boolean addToWishlist(Long userId, Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        Book book = bookOpt.get();

        // Check if already in wishlist
        if (wishlistItemRepository.existsByUserAndBook(user, book)) {
            return false; // Already in wishlist
        }

        // Create wishlist item
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setUser(user);
        wishlistItem.setBook(book);
        wishlistItem.setAddedAt(LocalDateTime.now());

        wishlistItemRepository.save(wishlistItem);
        return true;
    }

    /**
     * Remove a book from a user's wishlist (using IDs)
     */
    @Transactional
    public boolean removeFromWishlist(Long userId, Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        Book book = bookOpt.get();

        // Find the wishlist item
        Optional<WishlistItem> wishlistItemOpt = wishlistItemRepository.findByUserAndBook(user, book);

        if (wishlistItemOpt.isEmpty()) {
            return false; // Not in wishlist
        }

        // Delete the wishlist item
        wishlistItemRepository.delete(wishlistItemOpt.get());
        return true;
    }

    /**
     * Remove a wishlist item by ID
     */
    public void removeFromWishlist(Long wishlistItemId) {
        wishlistItemRepository.deleteById(wishlistItemId);
    }

    /**
     * Check if a book is in a user's wishlist
     */
    public boolean isBookInWishlist(User user, Book book) {
        return wishlistItemRepository.existsByUserAndBook(user, book);
    }

    /**
     * Get a user's wishlist as a list of BookDTOs
     */
    public List<BookDTO> getUserWishlist(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }

        User user = userOpt.get();
        List<WishlistItem> wishlistItems = wishlistItemRepository.findByUser(user);
        List<BookDTO> bookDTOs = new ArrayList<>();

        for (WishlistItem item : wishlistItems) {
            Book book = item.getBook();
            BookDTO bookDTO = convertToDTO(book);
            bookDTOs.add(bookDTO);
        }

        return bookDTOs;
    }

    /**
     * Convert a Book entity to a BookDTO
     */
    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setDescription(book.getDescription());
        dto.setCategory(book.getCategory());
        dto.setPrice(book.getPrice());
        dto.setStock(book.getStock());
        dto.setCoinPrice(book.getCoinPrice());
        // Set other properties as needed
        return dto;
    }
}