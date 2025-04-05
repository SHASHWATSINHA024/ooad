package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add placeholder stats
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", 0);
        stats.put("totalUsers", 0);
        stats.put("activeLoans", 0);
        stats.put("overdueBooks", 0);
        model.addAttribute("stats", stats);
        
        // Add empty collections for lists
        model.addAttribute("recentActivities", Collections.emptyList());
        model.addAttribute("popularBooks", Collections.emptyList());
        model.addAttribute("users", Collections.emptyList());
        
        return "admin/dashboard";
    }
    
    @GetMapping("/users")
    public String users(Model model) {
        // Add user management data
        return "admin/users";
    }
    
    @GetMapping("/books")
    public String books(Model model) {
        // Add book management data
        return "admin/books";
    }
    
    @GetMapping("/transactions")
    public String transactions(Model model) {
        // Add transaction management data
        return "admin/transactions";
    }
}