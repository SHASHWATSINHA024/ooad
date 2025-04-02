package com.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.librarymanagement.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}