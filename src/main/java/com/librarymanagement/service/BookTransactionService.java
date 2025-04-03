package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.BookTransactionDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BookTransaction;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.BookTransaction.TransactionType;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BookTransactionRepository;
import com.librarymanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookTransactionService {

    @Autowired
    private BookTransactionRepository bookTransactionRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    // Default due date period in days
    private static final int DEFAULT_BORROW_PERIOD = 14;
    
    /**
     * Create a new book transaction
     */
    public BookTransactionDTO createTransaction(BookTransactionDTO transactionDTO) {
        Optional<Book> bookOpt = bookRepository.findById(transactionDTO.getBookId());
        Optional<User> userOpt = userRepository.findById(transactionDTO.getUserId());
        
        if (bookOpt.isEmpty() || userOpt.isEmpty()) {
            return null;
        }
        
        Book book = bookOpt.get();
        User user = userOpt.get();
        
        BookTransaction transaction = new BookTransaction();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setType(TransactionType.valueOf(transactionDTO.getType()));
        transaction.setTransactionDate(LocalDateTime.now());
        
        // Set due date for borrowed books
        if (transaction.getType() == TransactionType.BORROW) {
            transaction.setDueDate(LocalDateTime.now().plusDays(DEFAULT_BORROW_PERIOD));
            
            // Update book's borrow count
            book.setBorrowCount(book.getBorrowCount() + 1);
            bookRepository.save(book);
        }
        
        // Handle coin usage for purchases
        if (transaction.getType() == TransactionType.PURCHASE && transactionDTO.getCoinsUsed() > 0) {
            int coinsUsed = transactionDTO.getCoinsUsed();
            
            // Check if user has enough coins
            if (user.getCoins() >= coinsUsed) {
                transaction.setCoinsUsed(coinsUsed);
                user.setCoins(user.getCoins() - coinsUsed);
                userRepository.save(user);
            }
        }
        
        // Save the transaction
        BookTransaction savedTransaction = bookTransactionRepository.save(transaction);
        
        // Convert to DTO
        BookTransactionDTO savedTransactionDTO = new BookTransactionDTO();
        savedTransactionDTO.setId(savedTransaction.getId());
        savedTransactionDTO.setBookId(savedTransaction.getBook().getId());
        savedTransactionDTO.setBookTitle(savedTransaction.getBook().getTitle());
        savedTransactionDTO.setUserId(savedTransaction.getUser().getId());
        savedTransactionDTO.setUserName(savedTransaction.getUser().getUsername());
        savedTransactionDTO.setType(savedTransaction.getType().toString());
        savedTransactionDTO.setTransactionDate(savedTransaction.getTransactionDate());
        savedTransactionDTO.setDueDate(savedTransaction.getDueDate());
        savedTransactionDTO.setReturnDate(savedTransaction.getReturnDate());
        savedTransactionDTO.setCoinsUsed(savedTransaction.getCoinsUsed());
        savedTransactionDTO.setPaid(savedTransaction.isPaid());
        savedTransactionDTO.setNotes(savedTransaction.getNotes());
        
        // Calculate days left if it's a borrow transaction
        if (savedTransaction.getType() == TransactionType.BORROW && savedTransaction.getDueDate() != null) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), savedTransaction.getDueDate());
            savedTransactionDTO.setDaysLeft(daysLeft);
            savedTransactionDTO.setOverdue(daysLeft < 0);
        }
        
        return savedTransactionDTO;
    }
    
    /**
     * Get a transaction by ID
     */
    public BookTransactionDTO getTransactionById(Long id) {
        Optional<BookTransaction> transactionOpt = bookTransactionRepository.findById(id);
        
        if (transactionOpt.isEmpty()) {
            return null;
        }
        
        BookTransaction transaction = transactionOpt.get();
        
        BookTransactionDTO transactionDTO = new BookTransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setBookId(transaction.getBook().getId());
        transactionDTO.setBookTitle(transaction.getBook().getTitle());
        transactionDTO.setUserId(transaction.getUser().getId());
        transactionDTO.setUserName(transaction.getUser().getUsername());
        transactionDTO.setType(transaction.getType().toString());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        transactionDTO.setDueDate(transaction.getDueDate());
        transactionDTO.setReturnDate(transaction.getReturnDate());
        transactionDTO.setCoinsUsed(transaction.getCoinsUsed());
        transactionDTO.setPaid(transaction.isPaid());
        transactionDTO.setNotes(transaction.getNotes());
        
        // Calculate days left if it's a borrow transaction
        if (transaction.getType() == TransactionType.BORROW && transaction.getDueDate() != null) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), transaction.getDueDate());
            transactionDTO.setDaysLeft(daysLeft);
            transactionDTO.setOverdue(daysLeft < 0);
        }
        
        return transactionDTO;
    }
    
    /**
     * Get all transactions for a user
     */
    public List<BookTransactionDTO> getTransactionsByUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<BookTransaction> transactions = bookTransactionRepository.findByUser(userOpt.get());
        List<BookTransactionDTO> transactionDTOs = new ArrayList<>();
        
        for (BookTransaction transaction : transactions) {
            BookTransactionDTO transactionDTO = new BookTransactionDTO();
            transactionDTO.setId(transaction.getId());
            transactionDTO.setBookId(transaction.getBook().getId());
            transactionDTO.setBookTitle(transaction.getBook().getTitle());
            transactionDTO.setUserId(transaction.getUser().getId());
            transactionDTO.setUserName(transaction.getUser().getUsername());
            transactionDTO.setType(transaction.getType().toString());
            transactionDTO.setTransactionDate(transaction.getTransactionDate());
            transactionDTO.setDueDate(transaction.getDueDate());
            transactionDTO.setReturnDate(transaction.getReturnDate());
            transactionDTO.setCoinsUsed(transaction.getCoinsUsed());
            transactionDTO.setPaid(transaction.isPaid());
            transactionDTO.setNotes(transaction.getNotes());
            
            // Calculate days left if it's a borrow transaction
            if (transaction.getType() == TransactionType.BORROW && transaction.getDueDate() != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), transaction.getDueDate());
                transactionDTO.setDaysLeft(daysLeft);
                transactionDTO.setOverdue(daysLeft < 0);
            }
            
            transactionDTOs.add(transactionDTO);
        }
        
        return transactionDTOs;
    }
    
    /**
     * Get the most recent transaction for a user
     */
    public BookTransactionDTO getRecentTransaction(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return null;
        }
        
        List<BookTransaction> transactions = 
            bookTransactionRepository.findRecentTransactionsByUser(userOpt.get());
        
        if (transactions.isEmpty()) {
            return null;
        }
        
        BookTransaction transaction = transactions.get(0);
        
        BookTransactionDTO transactionDTO = new BookTransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setBookId(transaction.getBook().getId());
        transactionDTO.setBookTitle(transaction.getBook().getTitle());
        transactionDTO.setUserId(transaction.getUser().getId());
        transactionDTO.setUserName(transaction.getUser().getUsername());
        transactionDTO.setType(transaction.getType().toString());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        transactionDTO.setDueDate(transaction.getDueDate());
        transactionDTO.setReturnDate(transaction.getReturnDate());
        transactionDTO.setCoinsUsed(transaction.getCoinsUsed());
        transactionDTO.setPaid(transaction.isPaid());
        transactionDTO.setNotes(transaction.getNotes());
        
        // Calculate days left if it's a borrow transaction
        if (transaction.getType() == TransactionType.BORROW && transaction.getDueDate() != null) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), transaction.getDueDate());
            transactionDTO.setDaysLeft(daysLeft);
            transactionDTO.setOverdue(daysLeft < 0);
        }
        
        return transactionDTO;
    }
    
    /**
     * Get currently borrowed books by a user
     */
    public List<BookTransactionDTO> getCurrentBorrows(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<BookTransaction> transactions = 
            bookTransactionRepository.findCurrentBorrowsByUser(userOpt.get());
        
        List<BookTransactionDTO> transactionDTOs = new ArrayList<>();
        
        for (BookTransaction transaction : transactions) {
            BookTransactionDTO transactionDTO = new BookTransactionDTO();
            transactionDTO.setId(transaction.getId());
            transactionDTO.setBookId(transaction.getBook().getId());
            transactionDTO.setBookTitle(transaction.getBook().getTitle());
            transactionDTO.setUserId(transaction.getUser().getId());
            transactionDTO.setUserName(transaction.getUser().getUsername());
            transactionDTO.setType(transaction.getType().toString());
            transactionDTO.setTransactionDate(transaction.getTransactionDate());
            transactionDTO.setDueDate(transaction.getDueDate());
            transactionDTO.setReturnDate(transaction.getReturnDate());
            transactionDTO.setCoinsUsed(transaction.getCoinsUsed());
            transactionDTO.setPaid(transaction.isPaid());
            transactionDTO.setNotes(transaction.getNotes());
            
            // Calculate days left
            if (transaction.getDueDate() != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), transaction.getDueDate());
                transactionDTO.setDaysLeft(daysLeft);
                transactionDTO.setOverdue(daysLeft < 0);
            }
            
            transactionDTOs.add(transactionDTO);
        }
        
        return transactionDTOs;
    }
    
    /**
     * Return a book
     */
    public BookTransactionDTO returnBook(Long transactionId) {
        Optional<BookTransaction> transactionOpt = bookTransactionRepository.findById(transactionId);
        
        if (transactionOpt.isEmpty()) {
            return null;
        }
        
        BookTransaction transaction = transactionOpt.get();
        
        // Check if it's a borrow transaction and hasn't been returned yet
        if (transaction.getType() != TransactionType.BORROW || transaction.getReturnDate() != null) {
            return null;
        }
        
        // Mark as returned
        transaction.setReturnDate(LocalDateTime.now());
        
        // Save the transaction
        BookTransaction savedTransaction = bookTransactionRepository.save(transaction);
        
        // Convert to DTO
        BookTransactionDTO transactionDTO = new BookTransactionDTO();
        transactionDTO.setId(savedTransaction.getId());
        transactionDTO.setBookId(savedTransaction.getBook().getId());
        transactionDTO.setBookTitle(savedTransaction.getBook().getTitle());
        transactionDTO.setUserId(savedTransaction.getUser().getId());
        transactionDTO.setUserName(savedTransaction.getUser().getUsername());
        transactionDTO.setType(savedTransaction.getType().toString());
        transactionDTO.setTransactionDate(savedTransaction.getTransactionDate());
        transactionDTO.setDueDate(savedTransaction.getDueDate());
        transactionDTO.setReturnDate(savedTransaction.getReturnDate());
        transactionDTO.setCoinsUsed(savedTransaction.getCoinsUsed());
        transactionDTO.setPaid(savedTransaction.isPaid());
        transactionDTO.setNotes(savedTransaction.getNotes());
        
        return transactionDTO;
    }
    
    /**
     * Get all overdue books
     */
    public List<BookTransactionDTO> getOverdueBooks() {
        List<BookTransaction> transactions = 
            bookTransactionRepository.findOverdueBooks(LocalDateTime.now());
        
        List<BookTransactionDTO> transactionDTOs = new ArrayList<>();
        
        for (BookTransaction transaction : transactions) {
            BookTransactionDTO transactionDTO = new BookTransactionDTO();
            transactionDTO.setId(transaction.getId());
            transactionDTO.setBookId(transaction.getBook().getId());
            transactionDTO.setBookTitle(transaction.getBook().getTitle());
            transactionDTO.setUserId(transaction.getUser().getId());
            transactionDTO.setUserName(transaction.getUser().getUsername());
            transactionDTO.setType(transaction.getType().toString());
            transactionDTO.setTransactionDate(transaction.getTransactionDate());
            transactionDTO.setDueDate(transaction.getDueDate());
            transactionDTO.setReturnDate(transaction.getReturnDate());
            transactionDTO.setCoinsUsed(transaction.getCoinsUsed());
            transactionDTO.setPaid(transaction.isPaid());
            transactionDTO.setNotes(transaction.getNotes());
            
            // Calculate days overdue
            if (transaction.getDueDate() != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), transaction.getDueDate());
                transactionDTO.setDaysLeft(daysLeft);
                transactionDTO.setOverdue(true);
            }
            
            transactionDTOs.add(transactionDTO);
        }
        
        return transactionDTOs;
    }
    
    /**
     * Calculate earnings between dates
     */
    public Integer calculateEarnings(LocalDateTime startDate, LocalDateTime endDate) {
        return bookTransactionRepository.findTotalEarnings(startDate, endDate);
    }
} 