package com.librarymanagement.controller;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam Long id,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String address,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Get current user from session
            UserDTO sessionUser = (UserDTO) session.getAttribute("currentUser");
            
            // Security check - only update own profile
            if (sessionUser == null || !sessionUser.getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access");
                return "redirect:/auth/login";
            }
            
            // Create updated user object
            UserDTO updatedUser = new UserDTO();
            updatedUser.setId(id);
            updatedUser.setFullName(fullName);
            updatedUser.setEmail(email);
            updatedUser.setPhoneNumber(phoneNumber);
            updatedUser.setAddress(address);
            
            // Update user in database
            UserDTO result = userService.updateUser(id, updatedUser);
            
            if (result != null) {
                // Update session with new user data
                session.setAttribute("currentUser", result);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to update profile");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        }
        
        return "redirect:/users/dashboard";
    }
}