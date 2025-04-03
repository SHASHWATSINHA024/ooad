package com.librarymanagement.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagement.dto.LibrarianDTO;
import com.librarymanagement.dto.LoginDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.LibrarianService;
import com.librarymanagement.service.UserService;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private LibrarianService librarianService;
    
    @PostMapping("/users/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginDTO loginDTO) {
        UserDTO user = userService.authenticateUser(loginDTO);
        
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    @PostMapping("/librarians/login")
    public ResponseEntity<?> librarianLogin(@Valid @RequestBody LoginDTO loginDTO) {
        LibrarianDTO librarian = librarianService.authenticateLibrarian(loginDTO);
        
        if (librarian != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("librarian", librarian);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful. Your membership request is pending approval.");
            response.put("user", registeredUser);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/librarians/register")
    public ResponseEntity<?> registerLibrarian(@Valid @RequestBody LibrarianDTO librarianDTO) {
        try {
            LibrarianDTO registeredLibrarian = librarianService.registerLibrarian(librarianDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Librarian registration successful");
            response.put("librarian", registeredLibrarian);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
} 