package com.librarymanagement.repository;

import com.librarymanagement.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {}
