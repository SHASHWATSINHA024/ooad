package com.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.librarymanagement.entity.BookDelivery;

public interface BookDeliveryRepository extends JpaRepository<BookDelivery, Long> {
}
