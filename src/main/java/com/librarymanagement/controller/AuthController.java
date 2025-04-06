package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully");
        return "redirect:/";
    }
}