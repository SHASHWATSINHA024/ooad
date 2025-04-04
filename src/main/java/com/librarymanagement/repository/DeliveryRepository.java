package com.librarymanagement.repository;

import com.librarymanagement.entity.BookDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<BookDelivery, Long> {}
