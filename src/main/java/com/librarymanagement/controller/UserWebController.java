package com.librarymanagement.controller;

import com.librarymanagement.entity.User;
import com.librarymanagement.service.UserService;
import com.librarymanagement.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserWebController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        // Get current user
        if (principal != null) {
            UserDTO userDTO = userService.getUserByUsername(principal.getName());
            model.addAttribute("user", userDTO);
            
            // Initialize empty lists if actual data isn't available yet
            model.addAttribute("borrowedBooks", new ArrayList<>());
            model.addAttribute("wishlist", new ArrayList<>());
            
            // Create a simple membership object
            Map<String, Object> membership = new HashMap<>();
            membership.put("active", true);
            membership.put("type", "Standard");
            model.addAttribute("membership", membership);
        }
        
        return "users/dashboard";
    }
}