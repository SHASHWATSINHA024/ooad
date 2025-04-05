package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.librarymanagement.entity.User;
import com.librarymanagement.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username, 
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {
        
        // For development: using users from DataInitializer
        if (username.equals("admin") && password.equals("admin123")) {
            return "redirect:/admin/dashboard";
        } else if (username.equals("librarian") && password.equals("librarian123")) {
            return "redirect:/librarian/dashboard";
        } else if (username.equals("user") && password.equals("user123")) {
            return "redirect:/users/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/auth/login";
        }
    }
    
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String processRegistration(
            /* Add form parameters here */
            RedirectAttributes redirectAttributes) {
        
        // Registration logic
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/auth/login";
    }
    
    @PostMapping("/logout")
    public String logout() {
        // Simple logout - in a real app, this would use Spring Security
        return "redirect:/";
    }
}