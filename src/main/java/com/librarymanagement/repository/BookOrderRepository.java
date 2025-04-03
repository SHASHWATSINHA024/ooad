package com.librarymanagement.repository;

import com.librarymanagement.entity.BookOrder;
import com.librarymanagement.entity.Librarian;
import com.librarymanagement.entity.BookOrder.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findByOrderedBy(Librarian librarian);
    List<BookOrder> findByStatus(OrderStatus status);
    List<BookOrder> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    List<BookOrder> findByStatusAndOrderDateBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);
} 