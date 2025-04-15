package com.librarymanagement.service;

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

    // Add book
    public String addBook(BookDTO bookDTO) {
        Book book = new Book();
        setBookDetails(book, bookDTO);  // Using the common method to set book details
        bookRepository.save(book);
        return "Book added successfully!";
    }

    // Get all books
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : books) {
            bookDTOs.add(convertToDTO(book));
        }
        return bookDTOs;
    }

    // Get book by ID
    public BookDTO getBook(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return null;
        }
        return convertToDTO(book);
    }

    // Get top books
    public List<BookDTO> getTopBooks(int limit) {
        List<Book> books = bookRepository.findTopBooks(); // Assuming this is a custom query in your repo
        List<BookDTO> bookDTOs = new ArrayList<>();
        int count = 0;
        for (Book book : books) {
            if (count >= limit) {
                break;
            }
            bookDTOs.add(convertToDTO(book));
            count++;
        }
        return bookDTOs;
    }

    // Update book
    public String updateBook(Long id, BookDTO bookDTO) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            return "Book not found";
        }
        Book book = bookOpt.get();
        setBookDetails(book, bookDTO);
        bookRepository.save(book);
        return "Book updated successfully!";
    }

    // Delete book
    public String deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            return "Book not found";
        }
        bookRepository.deleteById(id);
        return "Book deleted successfully!";
    }

    // Buy book
    public boolean buyBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null || book.getStock() <= 0) {
            return false; // Not found or no stock available
        }
        book.setStock(book.getStock() - 1);
        bookRepository.save(book);
        return true;
    }

    // Request book
    public boolean requestBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            return false; // Book not found
        }
        // You can add a request queue logic here
        return true;
    }

    // Convert entity to DTO
    private BookDTO convertToDTO(Book book) {
        Double avgRating = bookReviewRepository.findAverageRatingByBookId(book.getId());
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
            avgRating,
            book.getAvailableCopies(),  // Include availableCopies
            book.getTotalCopies()       // Include totalCopies
        );
    }

    // Set book details
    private void setBookDetails(Book book, BookDTO bookDTO) {
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setCategory(bookDTO.getCategory());
        book.setPrice(bookDTO.getPrice());
        book.setIsbn(bookDTO.getIsbn());
        book.setDescription(bookDTO.getDescription());
        book.setStock(bookDTO.getStock());
        book.setCoinPrice(bookDTO.getCoinPrice());
        book.setPublishDate(bookDTO.getPublishDate());
    }
}
