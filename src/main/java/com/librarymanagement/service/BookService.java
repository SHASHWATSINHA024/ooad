package com.librarymanagement.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BookReviewRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BookReviewRepository bookReviewRepository;

    public String addBook(BookDTO bookDTO) {
        try {
            // Input validation
            if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty()) {
                return "Book title is required";
            }
            
            if (bookDTO.getAuthor() == null || bookDTO.getAuthor().trim().isEmpty()) {
                return "Book author is required";
            }
            
            Book book = new Book();
            book.setTitle(bookDTO.getTitle());
            book.setAuthor(bookDTO.getAuthor());
            
            // Handle category
            if (bookDTO.getCategory() != null && !bookDTO.getCategory().trim().isEmpty()) {
                book.setCategory(bookDTO.getCategory());
            } else {
                book.setCategory("Uncategorized");
            }
            
            // Handle price
            if (bookDTO.getPrice() != null) {
                book.setPrice(bookDTO.getPrice());
            } else {
                book.setPrice(new BigDecimal("0.00"));
            }
            
            // Handle optional fields with null checks
            if (bookDTO.getIsbn() != null && !bookDTO.getIsbn().trim().isEmpty()) {
                book.setIsbn(bookDTO.getIsbn());
            }
            
            if (bookDTO.getDescription() != null && !bookDTO.getDescription().trim().isEmpty()) {
                book.setDescription(bookDTO.getDescription());
            }
            
            // Set default stock if not provided
            book.setStock(bookDTO.getStock() > 0 ? bookDTO.getStock() : 1);
            
            // Set default coin price if not provided
            book.setCoinPrice(bookDTO.getCoinPrice() > 0 ? bookDTO.getCoinPrice() : 0);
            
            // Set publish date if provided, otherwise use current date
            book.setPublishDate(bookDTO.getPublishDate() != null ? bookDTO.getPublishDate() : LocalDateTime.now());

            bookRepository.save(book);
            return "Book added successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOs = new ArrayList<>();
        
        for (Book book : books) {
            BookDTO bookDTO = convertToDTO(book);
            bookDTOs.add(bookDTO);
        }
        
        return bookDTOs;
    }

    public BookDTO getBook(Long id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            return null; // Or handle it with an exception
        }

        return convertToDTO(book);
    }
    
    public List<BookDTO> searchBooks(String keyword) {
        List<Book> books = bookRepository.findByKeyword(keyword);
        List<BookDTO> bookDTOs = new ArrayList<>();
        
        for (Book book : books) {
            BookDTO bookDTO = convertToDTO(book);
            bookDTOs.add(bookDTO);
        }
        
        return bookDTOs;
    }
    
    public List<BookDTO> getTopBooks(int limit) {
        List<Book> books = bookRepository.findTopBooks();
        List<BookDTO> bookDTOs = new ArrayList<>();
        
        // Limit the results
        int count = 0;
        for (Book book : books) {
            if (count >= limit) {
                break;
            }
            
            BookDTO bookDTO = convertToDTO(book);
            bookDTOs.add(bookDTO);
            count++;
        }
        
        return bookDTOs;
    }
    
    public String updateBook(Long id, BookDTO bookDTO) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        
        if (bookOpt.isEmpty()) {
            return "Book not found";
        }
        
        Book book = bookOpt.get();
        
        if (bookDTO.getTitle() != null) {
            book.setTitle(bookDTO.getTitle());
        }
        if (bookDTO.getAuthor() != null) {
            book.setAuthor(bookDTO.getAuthor());
        }
        if (bookDTO.getCategory() != null) {
            book.setCategory(bookDTO.getCategory());
        }
        if (bookDTO.getPrice() != null) {
            book.setPrice(bookDTO.getPrice());
        }
        if (bookDTO.getIsbn() != null) {
            book.setIsbn(bookDTO.getIsbn());
        }
        if (bookDTO.getDescription() != null) {
            book.setDescription(bookDTO.getDescription());
        }
        if (bookDTO.getStock() > 0) {
            book.setStock(bookDTO.getStock());
        }
        if (bookDTO.getCoinPrice() >= 0) {
            book.setCoinPrice(bookDTO.getCoinPrice());
        }
        if (bookDTO.getPublishDate() != null) {
            book.setPublishDate(bookDTO.getPublishDate());
        }
        
        bookRepository.save(book);
        return "Book updated successfully!";
    }
    
    public String deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            return "Book not found";
        }
        
        bookRepository.deleteById(id);
        return "Book deleted successfully!";
    }
    
    // Helper method to convert Book entity to BookDTO
    private BookDTO convertToDTO(Book book) {
        Double avgRating = null;
        if (bookReviewRepository != null) {
            avgRating = bookReviewRepository.findAverageRatingByBookId(book.getId());
        }
        
        return new BookDTO(
            book.getId(),
            book.getTitle(), 
            book.getAuthor(), 
            book.getIsbn(),
            book.getDescription(),
            book.getCategory(), 
            book.getPrice(),
            book.getStock(),
            book.getCoinPrice(),
            book.getBorrowCount(),
            book.getPublishDate(),
            avgRating
        );
    }
}
