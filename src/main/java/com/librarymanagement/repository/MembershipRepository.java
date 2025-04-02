package com.librarymanagement.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.librarymanagement.entity.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByIsActiveTrue();
    List<Membership> findByIsActiveFalse();
}
