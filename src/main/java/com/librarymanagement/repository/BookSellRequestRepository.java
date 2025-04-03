package com.librarymanagement.repository;

import com.librarymanagement.entity.BookSellRequest;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.BookSellRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSellRequestRepository extends JpaRepository<BookSellRequest, Long> {
    List<BookSellRequest> findByUser(User user);
    List<BookSellRequest> findByStatus(RequestStatus status);
    List<BookSellRequest> findByUserAndStatus(User user, RequestStatus status);
} 