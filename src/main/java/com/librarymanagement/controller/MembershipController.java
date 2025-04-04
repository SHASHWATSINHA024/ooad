package com.librarymanagement.controller;

import com.librarymanagement.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @PostMapping("/create")
    public String createMembership(@RequestParam Long userId) {
        return membershipService.createMembership(userId);
    }

    @PostMapping("/cancel")
    public String cancelMembership(@RequestParam Long userId) {
        return membershipService.cancelMembership(userId);
    }
}
// Compare this snippet from src/main/java/com/librarymanagement/service/MembershipService.java:
// package com.librarymanagement.service;