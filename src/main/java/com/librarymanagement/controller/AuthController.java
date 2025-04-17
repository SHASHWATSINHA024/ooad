package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.librarymanagement.dto.LoginDTO;
import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(LoginDTO loginDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        UserDTO user = userService.authenticateUser(loginDTO);
        
        if (user != null) {
            // Store user in session
            session.setAttribute("currentUser", user);
            
            // Redirect based on role
            switch (user.getRole().toUpperCase()) {
                case "ADMIN":
                    return "redirect:/admin/dashboard";
                case "LIBRARIAN":
                    return "redirect:/librarian/dashboard";
                case "USER":
                default:
                    return "redirect:/users/dashboard";
            }
        } else {
            // Authentication failed
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model, @RequestParam(required = false) String membershipType) {
        UserDTO userDTO = new UserDTO();
        
        // Pre-select membership type if provided in URL
        if (membershipType != null && !membershipType.isEmpty()) {
            userDTO.setMembershipType(membershipType);
        }
        
        model.addAttribute("userDTO", userDTO);
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute UserDTO userDTO, 
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      RedirectAttributes redirectAttributes) {
        
        // Validate password match
        if (!userDTO.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/auth/register";
        }
        
        // Set initial status as PENDING for librarian approval
        userDTO.setStatus("PENDING");
        
        // Set default role as USER
        userDTO.setRole("USER");
        
        // Set initial coins
        userDTO.setCoins(0);
        
        try {
            // Create user
            UserDTO createdUser = userService.registerUser(userDTO);
            
            if (createdUser != null) {
                redirectAttributes.addFlashAttribute("message", 
                    "Registration successful! Your membership request has been sent to the librarian for approval.");
                return "redirect:/auth/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to register. Username or email may already be in use.");
                return "redirect:/auth/register";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully");
        return "redirect:/";
    }
}