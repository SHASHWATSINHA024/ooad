package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.BookSellRequestDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BookSellRequest;
import com.librarymanagement.entity.Librarian;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.BookSellRequest.RequestStatus;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BookSellRequestRepository;
import com.librarymanagement.repository.LibrarianRepository;
import com.librarymanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookSellRequestService {

    @Autowired
    private BookSellRequestRepository bookSellRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LibrarianRepository librarianRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    /**
     * Create a new book sell request
     */
    public BookSellRequestDTO createSellRequest(BookSellRequestDTO requestDTO) {
        Optional<User> userOpt = userRepository.findById(requestDTO.getUserId());
        
        if (userOpt.isEmpty()) {
            return null;
        }
        
        User user = userOpt.get();
        
        BookSellRequest request = new BookSellRequest();
        request.setUser(user);
        request.setBookTitle(requestDTO.getBookTitle());
        request.setBookAuthor(requestDTO.getBookAuthor());
        request.setIsbn(requestDTO.getIsbn());
        request.setBookCondition(requestDTO.getBookCondition());
        request.setAskingPrice(requestDTO.getAskingPrice());
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        request.setNotes(requestDTO.getNotes());
        
        BookSellRequest savedRequest = bookSellRequestRepository.save(request);
        
        return convertToDTO(savedRequest);
    }
    
    /**
     * Get all sell requests
     */
    public List<BookSellRequestDTO> getAllSellRequests() {
        List<BookSellRequest> requests = bookSellRequestRepository.findAll();
        List<BookSellRequestDTO> requestDTOs = new ArrayList<>();
        
        for (BookSellRequest request : requests) {
            requestDTOs.add(convertToDTO(request));
        }
        
        return requestDTOs;
    }
    
    /**
     * Get pending sell requests
     */
    public List<BookSellRequestDTO> getPendingSellRequests() {
        List<BookSellRequest> requests = bookSellRequestRepository.findByStatus(RequestStatus.PENDING);
        List<BookSellRequestDTO> requestDTOs = new ArrayList<>();
        
        for (BookSellRequest request : requests) {
            requestDTOs.add(convertToDTO(request));
        }
        
        return requestDTOs;
    }
    
    /**
     * Get a user's sell requests
     */
    public List<BookSellRequestDTO> getUserSellRequests(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }
        
        User user = userOpt.get();
        List<BookSellRequest> requests = bookSellRequestRepository.findByUser(user);
        List<BookSellRequestDTO> requestDTOs = new ArrayList<>();
        
        for (BookSellRequest request : requests) {
            requestDTOs.add(convertToDTO(request));
        }
        
        return requestDTOs;
    }
    
    /**
     * Get a sell request by ID
     */
    public BookSellRequestDTO getSellRequestById(Long id) {
        Optional<BookSellRequest> requestOpt = bookSellRequestRepository.findById(id);
        
        if (requestOpt.isEmpty()) {
            return null;
        }
        
        return convertToDTO(requestOpt.get());
    }
    
    /**
     * Approve a sell request
     */
    public BookSellRequestDTO approveSellRequest(Long id, Long librarianId, String notes) {
        Optional<BookSellRequest> requestOpt = bookSellRequestRepository.findById(id);
        Optional<Librarian> librarianOpt = librarianRepository.findById(librarianId);
        
        if (requestOpt.isEmpty() || librarianOpt.isEmpty()) {
            return null;
        }
        
        BookSellRequest request = requestOpt.get();
        Librarian librarian = librarianOpt.get();
        
        // Update request
        request.setStatus(RequestStatus.APPROVED);
        request.setReviewedBy(librarian);
        request.setReviewedDate(LocalDateTime.now());
        if (notes != null && !notes.isEmpty()) {
            request.setNotes(notes);
        }
        
        BookSellRequest savedRequest = bookSellRequestRepository.save(request);
        
        // Create book from the request
        Book book = new Book();
        book.setTitle(request.getBookTitle());
        book.setAuthor(request.getBookAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getAskingPrice());
        book.setStock(1);
        bookRepository.save(book);
        
        return convertToDTO(savedRequest);
    }
    
    /**
     * Reject a sell request
     */
    public BookSellRequestDTO rejectSellRequest(Long id, Long librarianId, String notes) {
        Optional<BookSellRequest> requestOpt = bookSellRequestRepository.findById(id);
        Optional<Librarian> librarianOpt = librarianRepository.findById(librarianId);
        
        if (requestOpt.isEmpty() || librarianOpt.isEmpty()) {
            return null;
        }
        
        BookSellRequest request = requestOpt.get();
        Librarian librarian = librarianOpt.get();
        
        // Update request
        request.setStatus(RequestStatus.REJECTED);
        request.setReviewedBy(librarian);
        request.setReviewedDate(LocalDateTime.now());
        if (notes != null && !notes.isEmpty()) {
            request.setNotes(notes);
        }
        
        BookSellRequest savedRequest = bookSellRequestRepository.save(request);
        
        return convertToDTO(savedRequest);
    }
    
    // Helper method to convert entity to DTO
    private BookSellRequestDTO convertToDTO(BookSellRequest request) {
        BookSellRequestDTO requestDTO = new BookSellRequestDTO();
        requestDTO.setId(request.getId());
        requestDTO.setUserId(request.getUser().getId());
        requestDTO.setUserName(request.getUser().getUsername());
        requestDTO.setBookTitle(request.getBookTitle());
        requestDTO.setBookAuthor(request.getBookAuthor());
        requestDTO.setIsbn(request.getIsbn());
        requestDTO.setBookCondition(request.getBookCondition());
        requestDTO.setAskingPrice(request.getAskingPrice());
        requestDTO.setRequestDate(request.getRequestDate());
        requestDTO.setStatus(request.getStatus().toString());
        requestDTO.setNotes(request.getNotes());
        requestDTO.setReviewedDate(request.getReviewedDate());
        
        if (request.getReviewedBy() != null) {
            requestDTO.setReviewedById(request.getReviewedBy().getId());
            requestDTO.setReviewedByName(request.getReviewedBy().getUsername());
        }
        
        return requestDTO;
    }
} 