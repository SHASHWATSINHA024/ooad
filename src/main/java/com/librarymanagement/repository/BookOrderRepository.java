package com.librarymanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.entity.BookOrder;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.BookOrder.OrderStatus;

@Repository
public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findByOrderedBy(User user);
    List<BookOrder> findByStatus(OrderStatus status);
    List<BookOrder> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    List<BookOrder> findByStatusAndOrderDateBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);
} 