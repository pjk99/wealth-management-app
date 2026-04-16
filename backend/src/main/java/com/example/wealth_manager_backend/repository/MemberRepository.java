package com.example.wealth_manager_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wealth_manager_backend.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {}

