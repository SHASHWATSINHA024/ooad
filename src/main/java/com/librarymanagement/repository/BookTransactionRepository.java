package com.librarymanagement.repository;

import com.librarymanagement.entity.BookTransaction;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.BookTransaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookTransactionRepository extends JpaRepository<BookTransaction, Long> {
    List<BookTransaction> findByUser(User user);
    List<BookTransaction> findByBook(Book book);
    List<BookTransaction> findByType(TransactionType type);
    
    @Query("SELECT t FROM BookTransaction t WHERE t.user = :user AND t.book = :book AND t.type = :type " +
           "AND t.returnDate IS NULL ORDER BY t.transactionDate DESC")
    List<BookTransaction> findActiveTransactions(User user, Book book, TransactionType type);
    
    @Query("SELECT t FROM BookTransaction t WHERE t.type = 'BORROW' AND t.dueDate < :now AND t.returnDate IS NULL")
    List<BookTransaction> findOverdueBooks(LocalDateTime now);
    
    @Query("SELECT t FROM BookTransaction t WHERE t.user = :user AND t.type = 'BORROW' AND t.returnDate IS NULL " +
           "ORDER BY t.transactionDate DESC")
    List<BookTransaction> findCurrentBorrowsByUser(User user);
    
    @Query("SELECT SUM(t.coinsUsed) FROM BookTransaction t WHERE t.transactionDate >= :startDate AND t.transactionDate <= :endDate")
    Integer findTotalEarnings(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT t FROM BookTransaction t WHERE t.user = :user ORDER BY t.transactionDate DESC")
    List<BookTransaction> findRecentTransactionsByUser(User user);
} 