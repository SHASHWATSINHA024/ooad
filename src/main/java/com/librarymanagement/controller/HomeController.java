package com.librarymanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    // REMOVE OR CHANGE THIS METHOD:
    // @GetMapping("/books")
    // public String books(Model model) {
    //     return "books/list";
    // }
    
    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("message", "Thymeleaf is working!");
        return "test";
    }
}