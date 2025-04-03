package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.LoginDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.User.UserStatus;
import com.librarymanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO registerUser(UserDTO userDTO) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create and save the user
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getFullName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setCoins(0);
        user.setStatus(UserStatus.PENDING);
        
        User savedUser = userRepository.save(user);
        
        // Convert to DTO and return
        UserDTO savedUserDTO = new UserDTO();
        savedUserDTO.setId(savedUser.getId());
        savedUserDTO.setUsername(savedUser.getUsername());
        savedUserDTO.setEmail(savedUser.getEmail());
        savedUserDTO.setFullName(savedUser.getFullName());
        savedUserDTO.setCoins(savedUser.getCoins());
        savedUserDTO.setStatus(savedUser.getStatus().toString());
        
        return savedUserDTO;
    }

    public UserDTO authenticateUser(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByUsername(loginDTO.getUsername());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                // Password matches, return user data
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setUsername(user.getUsername());
                userDTO.setEmail(user.getEmail());
                userDTO.setFullName(user.getFullName());
                userDTO.setCoins(user.getCoins());
                userDTO.setStatus(user.getStatus().toString());
                
                return userDTO;
            }
        }
        
        // Authentication failed
        return null;
    }

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
            userDTO.setStatus(user.getStatus().toString());
            
            return userDTO;
        }
        
        return null;
    }

    public UserDTO getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setFullName(user.getFullName());
            userDTO.setCoins(user.getCoins());
            userDTO.setStatus(user.getStatus().toString());
            
            return userDTO;
        }
        
        return null;
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(id);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Update the user fields
            if (userDTO.getFullName() != null) {
                user.setFullName(userDTO.getFullName());
            }
            
            if (userDTO.getEmail() != null) {
                // Check if email is already used by another user
                if (!user.getEmail().equals(userDTO.getEmail()) && 
                    userRepository.existsByEmail(userDTO.getEmail())) {
                    throw new IllegalArgumentException("Email already exists");
                }
                user.setEmail(userDTO.getEmail());
            }
            
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            
            if (userDTO.getStatus() != null) {
                try {
                    user.setStatus(UserStatus.valueOf(userDTO.getStatus()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid status value");
                }
            }
            
            User updatedUser = userRepository.save(user);
            
            // Convert to DTO and return
            UserDTO updatedUserDTO = new UserDTO();
            updatedUserDTO.setId(updatedUser.getId());
            updatedUserDTO.setUsername(updatedUser.getUsername());
            updatedUserDTO.setEmail(updatedUser.getEmail());
            updatedUserDTO.setFullName(updatedUser.getFullName());
            updatedUserDTO.setCoins(updatedUser.getCoins());
            updatedUserDTO.setStatus(updatedUser.getStatus().toString());
            
            return updatedUserDTO;
        }
        
        return null;
    }

    public Integer getUserCoins(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            return userOpt.get().getCoins();
        }
        
        return null;
    }

    public boolean addCoins(Long userId, int coins) {
        if (coins <= 0) {
            return false;
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setCoins(user.getCoins() + coins);
            userRepository.save(user);
            return true;
        }
        
        return false;
    }

    public boolean useCoins(Long userId, int coins) {
        if (coins <= 0) {
            return false;
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Check if the user has enough coins
            if (user.getCoins() >= coins) {
                user.setCoins(user.getCoins() - coins);
                userRepository.save(user);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Get all active users
     */
    public List<UserDTO> getAllActiveUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        
        for (User user : users) {
            if (user.getStatus() == UserStatus.ACTIVE) {
                UserDTO dto = new UserDTO();
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                dto.setEmail(user.getEmail());
                dto.setFullName(user.getFullName());
                dto.setCoins(user.getCoins());
                dto.setStatus(user.getStatus().toString());
                userDTOs.add(dto);
            }
        }
        
        return userDTOs;
    }
}
