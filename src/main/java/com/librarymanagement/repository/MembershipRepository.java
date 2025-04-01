package com.librarymanagement.repository;

import com.librarymanagement.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {}
