package com.librarymanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.librarymanagement.entity.Librarian;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.User.UserStatus;
import com.librarymanagement.repository.LibrarianRepository;
import com.librarymanagement.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LibrarianRepository librarianRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setName("Admin User");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setEmail("admin@library.com");
            adminUser.setFullName("System Administrator");
            adminUser.setStatus(UserStatus.ACTIVE);
            userRepository.save(adminUser);
            System.out.println("Admin user created with username: admin and password: admin123");
        }
        
        // Create a librarian if it doesn't exist
        if (!librarianRepository.existsByUsername("librarian")) {
            Librarian librarian = new Librarian();
            librarian.setUsername("librarian");
            librarian.setPassword(passwordEncoder.encode("librarian123"));
            librarian.setEmail("librarian@library.com");
            librarian.setFullName("Head Librarian");
            librarian.setEmployeeId("LIB001");
            librarian.setRole("ROLE_LIBRARIAN");
            librarianRepository.save(librarian);
            System.out.println("Librarian created with username: librarian and password: librarian123");
        }
        
        // Create a test user if it doesn't exist
        if (!userRepository.existsByUsername("user")) {
            User testUser = new User();
            testUser.setUsername("user");
            testUser.setName("Test User");
            testUser.setPassword(passwordEncoder.encode("user123"));
            testUser.setEmail("user@example.com");
            testUser.setFullName("Test User");
            testUser.setStatus(UserStatus.ACTIVE);
            userRepository.save(testUser);
            System.out.println("Test user created with username: user and password: user123");
        }
    }
} 