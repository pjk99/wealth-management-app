package com.example.wealth_manager_backend.service;

import org.springframework.stereotype.Service;

import com.example.wealth_manager_backend.dto.MemberRequest;
import com.example.wealth_manager_backend.entity.Household;
import com.example.wealth_manager_backend.entity.Member;
import com.example.wealth_manager_backend.repository.HouseholdRepository;
import com.example.wealth_manager_backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final HouseholdRepository householdRepository;
    private final MemberRepository memberRepository;

    public Member addMember(Long householdId, MemberRequest request) {

        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new RuntimeException("Household not found"));

        Member member = new Member();
        member.setName(request.getName());
        member.setRelationship(request.getRelationship());
        member.setDateOfBirth(request.getDateOfBirth());

        // Important: set relationship BOTH ways
        member.setHousehold(household);
        household.getMembers().add(member);

        return memberRepository.save(member);
    }
}
