package com.librarymanagement.service;

import org.springframework.stereotype.Service;

@Service
public class MembershipService {

    public String createMembership(Long userId) {
        // Business logic to create membership
        return "Membership created successfully!";
    }

    public String cancelMembership(Long userId) {
        // Business logic to cancel membership
        return "Membership cancelled successfully!";
    }
}
