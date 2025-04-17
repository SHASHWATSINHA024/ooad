package com.librarymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.librarymanagement.dto.UserDTO;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String redirectToDashboard(HttpSession session, RedirectAttributes redirectAttributes) {
        UserDTO user = (UserDTO) session.getAttribute("currentUser");
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to access your dashboard");
            return "redirect:/auth/login";
        }
        
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
    }
    
    @GetMapping("/")
    public String homePage() {
        return "index";
    }
    
    @GetMapping("/membership")
    public String membershipPage(Model model) {
        model.addAttribute("pageTitle", "Library Membership");
        return "membership";
    }
    
    @GetMapping("/events")
    public String eventsPage(Model model) {
        model.addAttribute("pageTitle", "Library Events");
        return "events";
    }
}