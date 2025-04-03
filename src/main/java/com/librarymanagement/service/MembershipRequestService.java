package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.MembershipRequestDTO;
import com.librarymanagement.entity.Librarian;
import com.librarymanagement.entity.MembershipRequest;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.MembershipRequest.RequestStatus;
import com.librarymanagement.repository.LibrarianRepository;
import com.librarymanagement.repository.MembershipRequestRepository;
import com.librarymanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MembershipRequestService {

    @Autowired
    private MembershipRequestRepository membershipRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LibrarianRepository librarianRepository;
    
    /**
     * Create a new membership request
     */
    public MembershipRequestDTO createRequest(MembershipRequestDTO requestDTO) {
        // Check if user exists
        Optional<User> userOpt = userRepository.findById(requestDTO.getUserId());
        if (userOpt.isEmpty()) {
            return null;
        }
        
        User user = userOpt.get();
        
        // Check if there's already a pending request for this user
        List<MembershipRequest> existingRequests = membershipRequestRepository.findByUserAndStatus(user, RequestStatus.PENDING);
        if (!existingRequests.isEmpty()) {
            return convertToDTO(existingRequests.get(0));
        }
        
        // Create new request
        MembershipRequest request = new MembershipRequest();
        request.setUser(user);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        request.setMembershipType(requestDTO.getMembershipType());
        request.setRequestMessage(requestDTO.getRequestMessage());
        
        MembershipRequest savedRequest = membershipRequestRepository.save(request);
        return convertToDTO(savedRequest);
    }
    
    /**
     * Get all membership requests
     */
    public List<MembershipRequestDTO> getAllRequests() {
        List<MembershipRequest> requests = membershipRequestRepository.findAll();
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get pending membership requests
     */
    public List<MembershipRequestDTO> getPendingRequests() {
        List<MembershipRequest> requests = membershipRequestRepository.findByStatus(RequestStatus.PENDING);
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get membership requests for a specific user
     */
    public List<MembershipRequestDTO> getUserRequests(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<MembershipRequest> requests = membershipRequestRepository.findByUser(userOpt.get());
        return requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific membership request by ID
     */
    public MembershipRequestDTO getRequestById(Long id) {
        Optional<MembershipRequest> requestOpt = membershipRequestRepository.findById(id);
        return requestOpt.map(this::convertToDTO).orElse(null);
    }
    
    /**
     * Approve a membership request
     */
    public MembershipRequestDTO approveRequest(Long id, Long librarianId, String notes) {
        Optional<MembershipRequest> requestOpt = membershipRequestRepository.findById(id);
        Optional<Librarian> librarianOpt = librarianRepository.findById(librarianId);
        
        if (requestOpt.isEmpty() || librarianOpt.isEmpty()) {
            return null;
        }
        
        MembershipRequest request = requestOpt.get();
        Librarian librarian = librarianOpt.get();
        
        // Only process if request is pending
        if (request.getStatus() != RequestStatus.PENDING) {
            return convertToDTO(request);
        }
        
        // Update request
        request.setStatus(RequestStatus.APPROVED);
        request.setProcessedBy(librarian);
        request.setProcessedDate(LocalDateTime.now());
        request.setAdminNotes(notes);
        
        // Update user
        User user = request.getUser();
        user.setMembershipType(request.getMembershipType());
        user.setMembershipStartDate(LocalDateTime.now());
        
        // Membership duration depends on type (1 year for standard, 2 years for premium)
        if ("PREMIUM".equalsIgnoreCase(request.getMembershipType())) {
            user.setMembershipEndDate(LocalDateTime.now().plusYears(2));
        } else {
            user.setMembershipEndDate(LocalDateTime.now().plusYears(1));
        }
        
        userRepository.save(user);
        MembershipRequest savedRequest = membershipRequestRepository.save(request);
        
        return convertToDTO(savedRequest);
    }
    
    /**
     * Reject a membership request
     */
    public MembershipRequestDTO rejectRequest(Long id, Long librarianId, String notes) {
        Optional<MembershipRequest> requestOpt = membershipRequestRepository.findById(id);
        Optional<Librarian> librarianOpt = librarianRepository.findById(librarianId);
        
        if (requestOpt.isEmpty() || librarianOpt.isEmpty()) {
            return null;
        }
        
        MembershipRequest request = requestOpt.get();
        Librarian librarian = librarianOpt.get();
        
        // Only process if request is pending
        if (request.getStatus() != RequestStatus.PENDING) {
            return convertToDTO(request);
        }
        
        // Update request
        request.setStatus(RequestStatus.REJECTED);
        request.setProcessedBy(librarian);
        request.setProcessedDate(LocalDateTime.now());
        request.setAdminNotes(notes);
        
        MembershipRequest savedRequest = membershipRequestRepository.save(request);
        
        return convertToDTO(savedRequest);
    }
    
    // Helper method to convert entity to DTO
    private MembershipRequestDTO convertToDTO(MembershipRequest request) {
        MembershipRequestDTO dto = new MembershipRequestDTO();
        dto.setId(request.getId());
        dto.setUserId(request.getUser().getId());
        dto.setUserName(request.getUser().getUsername());
        dto.setRequestDate(request.getRequestDate());
        dto.setStatus(request.getStatus().toString());
        dto.setMembershipType(request.getMembershipType());
        dto.setRequestMessage(request.getRequestMessage());
        dto.setProcessedDate(request.getProcessedDate());
        
        if (request.getProcessedBy() != null) {
            dto.setProcessedById(request.getProcessedBy().getId());
            dto.setProcessedByName(request.getProcessedBy().getUsername());
        }
        
        dto.setAdminNotes(request.getAdminNotes());
        
        return dto;
    }
} 