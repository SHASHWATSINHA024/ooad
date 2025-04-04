package com.librarymanagement.controller;

import com.librarymanagement.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return transactionService.borrowBook(userId, bookId);
    }

    @PostMapping("/return")
    public String returnBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return transactionService.returnBook(userId, bookId);
    }
}
