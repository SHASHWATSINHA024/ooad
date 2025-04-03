package com.librarymanagement.repository;

import com.librarymanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByCategoryIgnoreCase(String category);
    
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> findByKeyword(String keyword);
    
    @Query("SELECT b FROM Book b ORDER BY b.borrowCount DESC")
    List<Book> findTopBooks();
    
    @Query("SELECT b FROM Book b WHERE b.publishDate > :since ORDER BY b.publishDate DESC")
    List<Book> findRecentBooks(LocalDateTime since);
    
    List<Book> findByStockGreaterThan(int minStock);
    List<Book> findByStockLessThanEqual(int maxStock);

    List<Book> findByIsbn(String isbn);
}
