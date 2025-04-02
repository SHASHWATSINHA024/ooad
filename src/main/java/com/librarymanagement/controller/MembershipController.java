package com.librarymanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.librarymanagement.dto.MembershipDTO;
import com.librarymanagement.service.MembershipService;

@RestController
@RequestMapping("/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @PostMapping("/add")
    public MembershipDTO addMembership(@RequestBody MembershipDTO membershipDTO) {
        return membershipService.addMembership(membershipDTO);
    }

    @GetMapping("/{id}")
    public MembershipDTO getMembership(@PathVariable Long id) {
        return membershipService.getMembershipById(id);
    }

    @PutMapping("/update/{id}")
    public String updateMembershipStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        return membershipService.updateMembershipStatus(id, isActive);
    }

    // ✅ New Endpoint to Get All Memberships
    @GetMapping("/all")
    public List<MembershipDTO> getAllMemberships() {
        return membershipService.getAllMemberships();
    }
}
