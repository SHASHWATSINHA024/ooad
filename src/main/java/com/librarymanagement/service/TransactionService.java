package com.librarymanagement.service;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public String borrowBook(Long userId, Long bookId) {
        // Business logic for borrowing the book
        return "Book borrowed successfully!";
    }

    public String returnBook(Long userId, Long bookId) {
        // Business logic for returning the book
        return "Book returned successfully!";
    }
}
