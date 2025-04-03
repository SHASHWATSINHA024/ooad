package com.librarymanagement.repository;

import com.librarymanagement.entity.WishlistItem;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUser(User user);
    List<WishlistItem> findByBook(Book book);
    Optional<WishlistItem> findByUserAndBook(User user, Book book);
    boolean existsByUserAndBook(User user, Book book);
    void deleteByUserAndBook(User user, Book book);
} 