package com.librarymanagement.controller;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/users")
public class UserWebController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Get current user from session
        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        
        // Check if user is logged in
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to access your dashboard");
            return "redirect:/auth/login";
        }
        
        // Add user data to model
        model.addAttribute("user", user);
        
        // Add empty collections for now
        model.addAttribute("borrowedBooks", new ArrayList<>());
        model.addAttribute("wishlist", new ArrayList<>());
        
        return "users/dashboard";
    }
}