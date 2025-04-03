package com.librarymanagement.repository;

import com.librarymanagement.entity.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    Optional<Librarian> findByUsername(String username);
    Optional<Librarian> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 