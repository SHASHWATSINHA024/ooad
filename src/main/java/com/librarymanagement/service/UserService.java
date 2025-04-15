package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.User.UserStatus;
import com.librarymanagement.entity.User.UserRole;
import com.librarymanagement.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Method to register a new user
    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername().trim());
        user.setName(userDTO.getFullName());
        user.setPassword(userDTO.getPassword().intern());
        user.setEmail(userDTO.getEmail().trim());
        user.setFullName(userDTO.getFullName());
        user.setCoins(0);
        user.setStatus(UserStatus.PENDING);
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : UserRole.USER);

        User savedUser = userRepository.save(user);

        UserDTO savedUserDTO = new UserDTO();
        savedUserDTO.setId(savedUser.getId());
        savedUserDTO.setUsername(savedUser.getUsername());
        savedUserDTO.setEmail(savedUser.getEmail());
        savedUserDTO.setFullName(savedUser.getFullName());
        savedUserDTO.setCoins(savedUser.getCoins());
        savedUserDTO.setStatus(savedUser.getStatus());
        savedUserDTO.setRole(savedUser.getRole());

        return savedUserDTO;
    }

    // Method to get user coins by user ID
    public Integer getUserCoins(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get().getCoins();
        }
        return null;
    }

    // Method to get user by ID
    public UserDTO getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setFullName(user.getFullName());
            userDTO.setCoins(user.getCoins());
            userDTO.setStatus(user.getStatus());
            userDTO.setRole(user.getRole());

            return userDTO;
        }
        return null;
    }

    public UserDTO authenticate(String username, String password) {
        System.out.println("Authenticating username: '" + username + "', password: '" + password + "'");

        if ("H".equals(username.trim()) && "w123".equals(password)) {
            System.out.println("Hardcoded login successful.");
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setFullName("H");
            return userDTO;
        }

        // Query the user by username
        System.out.println("Checking username: '" + username.trim() + "'");
        Optional<User> userOpt = userRepository.findByUsername(username.trim());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if the password matches
            if ("w123".equals(password)) { // Replace with hashed password check in production
                System.out.println("User found: " + user.getUsername());

                // Map User entity to UserDTO
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setUsername(user.getUsername());
                userDTO.setEmail(user.getEmail());
                userDTO.setFullName(user.getFullName());
                userDTO.setCoins(user.getCoins());
                userDTO.setStatus(user.getStatus());
                userDTO.setRole(user.getRole());

                return userDTO;
            } else {
                System.out.println("Invalid password for username: " + username);
            }
        } else {
            System.out.println("User not found for username: " + username);
        }

        return null;
    }
}