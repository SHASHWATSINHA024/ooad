package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.librarymanagement.dto.BookSellRequestDTO;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BookSellRequest;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.BookSellRequest.RequestStatus;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BookSellRequestRepository;
import com.librarymanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookSellRequestService {

    @Autowired
    private BookSellRequestRepository bookSellRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Create a new book sell request
     */
    @Transactional
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

        try {
            BookSellRequest savedRequest = bookSellRequestRepository.saveAndFlush(request);
            return convertToDTO(savedRequest);
        } catch (Exception e) {
            System.err.println("Error saving BookSellRequest: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all sell requests
     */
    public List<BookSellRequestDTO> getAllSellRequests() {
        List<BookSellRequest> requests = bookSellRequestRepository.findAll();
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get pending sell requests
     */
    public List<BookSellRequestDTO> getPendingRequests() {
        List<BookSellRequest> requests = bookSellRequestRepository.findByStatus(RequestStatus.PENDING);
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get sell requests for a specific user
     */
    public List<BookSellRequestDTO> getUserRequests(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }

        List<BookSellRequest> requests = bookSellRequestRepository.findByUser(userOpt.get());
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific sell request by ID
     */
    public BookSellRequestDTO getRequestById(Long id) {
        Optional<BookSellRequest> requestOpt = bookSellRequestRepository.findById(id);
        return requestOpt.map(this::convertToDTO).orElse(null);
    }

    /**
     * Approve a sell request
     */
    @Transactional
    public BookSellRequestDTO approveSellRequest(Long id, Long adminId, String notes) {
        Optional<BookSellRequest> requestOpt = bookSellRequestRepository.findById(id);
        Optional<User> adminOpt = userRepository.findById(adminId);

        if (requestOpt.isEmpty() || adminOpt.isEmpty()) {
            return null;
        }

        BookSellRequest request = requestOpt.get();
        User admin = adminOpt.get();

        request.setStatus(RequestStatus.APPROVED);
        request.setReviewedBy(admin);
        request.setReviewedDate(LocalDateTime.now());
        if (notes != null && !notes.isEmpty()) {
            request.setNotes(notes);
        }

        BookSellRequest savedRequest = bookSellRequestRepository.saveAndFlush(request);

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
    @Transactional
    public BookSellRequestDTO rejectSellRequest(Long id, Long adminId, String notes) {
        Optional<BookSellRequest> requestOpt = bookSellRequestRepository.findById(id);
        Optional<User> adminOpt = userRepository.findById(adminId);

        if (requestOpt.isEmpty() || adminOpt.isEmpty()) {
            return null;
        }

        BookSellRequest request = requestOpt.get();
        User admin = adminOpt.get();

        request.setStatus(RequestStatus.REJECTED);
        request.setReviewedBy(admin);
        request.setReviewedDate(LocalDateTime.now());
        if (notes != null && !notes.isEmpty()) {
            request.setNotes(notes);
        }

        BookSellRequest savedRequest = bookSellRequestRepository.saveAndFlush(request);
        return convertToDTO(savedRequest);
    }

    /**
     * Save BookSellRequest directly
     */
    public BookSellRequest save(BookSellRequest request) {
        return bookSellRequestRepository.saveAndFlush(request);
    }

    /**
     * Convert entity to DTO
     */
    private BookSellRequestDTO convertToDTO(BookSellRequest request) {
        BookSellRequestDTO dto = new BookSellRequestDTO();
        dto.setId(request.getId());
        dto.setUserId(request.getUser().getId());
        dto.setBookTitle(request.getBookTitle());
        dto.setBookAuthor(request.getBookAuthor());
        dto.setIsbn(request.getIsbn());
        dto.setBookCondition(request.getBookCondition());
        dto.setAskingPrice(request.getAskingPrice());
        dto.setRequestDate(request.getRequestDate());
        dto.setStatus(request.getStatus().toString());
        dto.setReviewedDate(request.getReviewedDate());
        if (request.getReviewedBy() != null) {
            dto.setReviewedById(request.getReviewedBy().getId());
        }
        dto.setNotes(request.getNotes());
        return dto;
    }
}
