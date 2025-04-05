package com.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.librarymanagement.service.BookService;
import java.util.Collections;

@Controller
@RequestMapping("/books/view")  // Use a different path
public class BookWebController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public String listBooks(Model model) {
        try {
            model.addAttribute("books", bookService.getAllBooks());
        } catch (Exception e) {
            model.addAttribute("books", Collections.emptyList());
        }
        return "books/list";
    }
    
    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("book", bookService.getBook(id));
        } catch (Exception e) {
            // Handle error
        }
        return "books/detail";
    }
}