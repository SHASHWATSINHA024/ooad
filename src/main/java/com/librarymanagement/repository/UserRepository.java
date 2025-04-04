package com.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.librarymanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Add the method to find a user by their email
    User findByEmail(String email);
}
