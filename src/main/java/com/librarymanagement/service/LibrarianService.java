package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.LibrarianDTO;
import com.librarymanagement.dto.LoginDTO;
import com.librarymanagement.entity.Librarian;
import com.librarymanagement.repository.LibrarianRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private BookTransactionService bookTransactionService;
    
    public LibrarianDTO registerLibrarian(LibrarianDTO librarianDTO) {
        // Check if username or email already exists
        if (librarianRepository.existsByUsername(librarianDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (librarianRepository.existsByEmail(librarianDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create and save the librarian
        Librarian librarian = new Librarian();
        librarian.setUsername(librarianDTO.getUsername());
        librarian.setPassword(passwordEncoder.encode(librarianDTO.getPassword()));
        librarian.setEmail(librarianDTO.getEmail());
        librarian.setFullName(librarianDTO.getFullName());
        librarian.setAdmin(librarianDTO.isAdmin());
        
        Librarian savedLibrarian = librarianRepository.save(librarian);
        
        // Convert to DTO and return
        LibrarianDTO savedLibrarianDTO = new LibrarianDTO();
        savedLibrarianDTO.setId(savedLibrarian.getId());
        savedLibrarianDTO.setUsername(savedLibrarian.getUsername());
        savedLibrarianDTO.setEmail(savedLibrarian.getEmail());
        savedLibrarianDTO.setFullName(savedLibrarian.getFullName());
        savedLibrarianDTO.setAdmin(savedLibrarian.isAdmin());
        
        return savedLibrarianDTO;
    }

    public LibrarianDTO authenticateLibrarian(LoginDTO loginDTO) {
        Optional<Librarian> librarianOpt = librarianRepository.findByUsername(loginDTO.getUsername());
        
        if (librarianOpt.isPresent()) {
            Librarian librarian = librarianOpt.get();
            
            if (passwordEncoder.matches(loginDTO.getPassword(), librarian.getPassword())) {
                // Password matches, return librarian data
                LibrarianDTO librarianDTO = new LibrarianDTO();
                librarianDTO.setId(librarian.getId());
                librarianDTO.setUsername(librarian.getUsername());
                librarianDTO.setEmail(librarian.getEmail());
                librarianDTO.setFullName(librarian.getFullName());
                librarianDTO.setAdmin(librarian.isAdmin());
                
                return librarianDTO;
            }
        }
        
        // Authentication failed
        return null;
    }

    public LibrarianDTO getLibrarianById(Long id) {
        Optional<Librarian> librarianOpt = librarianRepository.findById(id);
        
        if (librarianOpt.isPresent()) {
            Librarian librarian = librarianOpt.get();
            
            LibrarianDTO librarianDTO = new LibrarianDTO();
            librarianDTO.setId(librarian.getId());
            librarianDTO.setUsername(librarian.getUsername());
            librarianDTO.setEmail(librarian.getEmail());
            librarianDTO.setFullName(librarian.getFullName());
            librarianDTO.setAdmin(librarian.isAdmin());
            
            return librarianDTO;
        }
        
        return null;
    }

    public Map<Month, Integer> getMonthlyEarnings(int year) {
        Map<Month, Integer> monthlyEarnings = new HashMap<>();
        Year requestedYear = Year.of(year);
        
        // Initialize all months with zero earnings
        for (Month month : Month.values()) {
            monthlyEarnings.put(month, 0);
        }
        
        // Calculate earnings for each month
        for (Month month : Month.values()) {
            LocalDateTime startDate = requestedYear.atMonth(month).atDay(1).atStartOfDay();
            LocalDateTime endDate = requestedYear.atMonth(month).atEndOfMonth().atTime(23, 59, 59);
            
            Integer earnings = bookTransactionService.calculateEarnings(startDate, endDate);
            
            // Update earnings for the month
            if (earnings != null) {
                monthlyEarnings.put(month, earnings);
            }
        }
        
        return monthlyEarnings;
    }

    public Integer getAnnualEarnings(int year) {
        Year requestedYear = Year.of(year);
        
        LocalDateTime startDate = requestedYear.atMonth(Month.JANUARY).atDay(1).atStartOfDay();
        LocalDateTime endDate = requestedYear.atMonth(Month.DECEMBER).atEndOfMonth().atTime(23, 59, 59);
        
        return bookTransactionService.calculateEarnings(startDate, endDate);
    }
} 