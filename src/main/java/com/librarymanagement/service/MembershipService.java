package com.librarymanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.librarymanagement.dto.MembershipDTO;
import com.librarymanagement.entity.Membership;
import com.librarymanagement.repository.MembershipRepository;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    public MembershipDTO addMembership(MembershipDTO membershipDTO) {
        Membership membership = new Membership(membershipDTO.getUserId(), membershipDTO.isActive());
        membership = membershipRepository.save(membership);
        return new MembershipDTO(membership.getId(), membership.getUserId(), membership.isActive());
    }

    public MembershipDTO getMembershipById(Long id) {
        return membershipRepository.findById(id)
            .map(membership -> new MembershipDTO(membership.getId(), membership.getUserId(), membership.isActive()))
            .orElse(null);
    }

    public String updateMembershipStatus(Long id, boolean isActive) {
        Membership membership = membershipRepository.findById(id).orElse(null);
        if (membership == null) {
            return "Membership not found!";
        }
        membership.setActive(isActive);
        membershipRepository.save(membership);
        return "Membership status updated!";
    }

    public List<MembershipDTO> getAllMemberships() {
        return membershipRepository.findAll().stream()
            .map(membership -> new MembershipDTO(membership.getId(), membership.getUserId(), membership.isActive()))
            .collect(Collectors.toList());
    }

    public List<MembershipDTO> getActiveMemberships() {
        return membershipRepository.findByIsActiveTrue().stream()
            .map(membership -> new MembershipDTO(membership.getId(), membership.getUserId(), membership.isActive()))
            .collect(Collectors.toList());
    }

    public List<MembershipDTO> getInactiveMemberships() {
        return membershipRepository.findByIsActiveFalse().stream()
            .map(membership -> new MembershipDTO(membership.getId(), membership.getUserId(), membership.isActive()))
            .collect(Collectors.toList());
    }
}
