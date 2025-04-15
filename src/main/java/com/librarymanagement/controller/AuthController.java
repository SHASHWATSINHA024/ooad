package com.librarymanagement.controller;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

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
    public String processLogin(String username, String password, 
                             HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            UserDTO user = userService.authenticate(username, password.intern());
            if (user != null) {
                session.setAttribute("currentUser", user);
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
                return "redirect:/auth/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/auth/login";
        }
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String processRegistration(UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.register(userDTO);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}