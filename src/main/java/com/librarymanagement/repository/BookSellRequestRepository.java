package com.librarymanagement.repository;

import com.librarymanagement.entity.BookSellRequest;
import com.librarymanagement.entity.BookSellRequest.RequestStatus;
import com.librarymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookSellRequestRepository extends JpaRepository<BookSellRequest, Long> {
    List<BookSellRequest> findByStatus(RequestStatus status);
    List<BookSellRequest> findByUser(User user);
}
