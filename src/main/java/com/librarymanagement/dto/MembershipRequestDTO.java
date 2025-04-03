package com.librarymanagement.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MembershipRequestDTO {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String userName;
    
    private String userEmail;
    
    private LocalDateTime requestDate;
    
    private String status;
    
    @NotEmpty(message = "Membership type is required")
    private String membershipType;
    
    private String requestMessage;
    
    private LocalDateTime processedDate;
    
    private Long processedById;
    
    private String processedByName;
    
    private String adminNotes;
    
    // Default constructor
    public MembershipRequestDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMembershipType() {
        return membershipType;
    }
    
    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }
    
    public String getRequestMessage() {
        return requestMessage;
    }
    
    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
    
    public LocalDateTime getProcessedDate() {
        return processedDate;
    }
    
    public void setProcessedDate(LocalDateTime processedDate) {
        this.processedDate = processedDate;
    }
    
    public Long getProcessedById() {
        return processedById;
    }
    
    public void setProcessedById(Long processedById) {
        this.processedById = processedById;
    }
    
    public String getProcessedByName() {
        return processedByName;
    }
    
    public void setProcessedByName(String processedByName) {
        this.processedByName = processedByName;
    }
    
    public String getAdminNotes() {
        return adminNotes;
    }
    
    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
} 