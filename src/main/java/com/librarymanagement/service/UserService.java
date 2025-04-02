package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.entity.User;
import com.librarymanagement.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register user with plain text password (not encoded)
    public void registerUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());  // ✅ Set name
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRole("USER"); // Default role if needed
        userRepository.save(user);
    }
    

    // Login user (password check with plain text password)
    public boolean loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password); // Compare plain text password
    }
}
