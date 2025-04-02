package com.librarymanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.BookDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.repository.BookRepository;
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public String addBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setCategory(bookDTO.getCategory());
        book.setPrice(bookDTO.getPrice());

        bookRepository.save(book);
        return "Book added successfully!";
    }
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookDTO getBook(Long id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            return null; // Or handle it with an exception
        }

        return new BookDTO(book.getTitle(), book.getAuthor(), book.getCategory(), book.getPrice());
    }
}
