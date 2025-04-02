package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return "User registered successfully!";
    }

    // Login endpoint
    @PostMapping("/login")
    public String loginUser(@RequestBody UserDTO userDTO) {
        boolean isAuthenticated = userService.loginUser(userDTO.getUsername(), userDTO.getPassword());
        return isAuthenticated ? "Login successful!" : "Invalid credentials!";
    }
    
}
