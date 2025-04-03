package com.librarymanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.entity.MembershipRequest;
import com.librarymanagement.entity.User;
import com.librarymanagement.entity.MembershipRequest.RequestStatus;

@Repository
public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, Long> {
    List<MembershipRequest> findByUser(User user);
    List<MembershipRequest> findByStatus(RequestStatus status);
    List<MembershipRequest> findByUserAndStatus(User user, RequestStatus status);
} 