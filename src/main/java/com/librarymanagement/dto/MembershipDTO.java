package com.librarymanagement.dto;

public class MembershipDTO {
    private Long id;
    private Long userId;
    private boolean isActive;

    // Constructors
    public MembershipDTO() {}

    public MembershipDTO(Long id, Long userId, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.isActive = isActive;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
