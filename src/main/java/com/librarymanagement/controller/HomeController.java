package com.librarymanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Library Management System API");
        response.put("version", "1.0");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("authentication", "/api/auth");
        endpoints.put("users", "/api/users");
        endpoints.put("librarians", "/api/librarians");
        endpoints.put("books", "/books");
        
        response.put("endpoints", endpoints);
        
        return response;
    }
    
    @GetMapping("/api")
    public Map<String, Object> api() {
        return home();
    }
} 