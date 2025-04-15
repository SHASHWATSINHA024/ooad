package com.librarymanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.librarymanagement.entity.User;
import com.librarymanagement.entity.User.UserRole;
import com.librarymanagement.entity.User.UserStatus;
import com.librarymanagement.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setName("Admin User");
                admin.setPassword(new BCryptPasswordEncoder().encode("admin123")); // Hash the password
                admin.setEmail("admin@library.com");
                admin.setFullName("System Administrator");
                admin.setCoins(0);
                admin.setStatus(UserStatus.ACTIVE);
                admin.setRole(UserRole.ADMIN);

                userRepository.save(admin);
                logger.info("Admin user created with username: admin and password: admin123");
            }
        } catch (Exception e) {
            logger.error("Error creating admin user", e);
        }
    }
}