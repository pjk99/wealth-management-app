package com.example.wealth_manager_backend.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wealth_manager_backend.dto.MemberRequest;
import com.example.wealth_manager_backend.entity.Member;
import com.example.wealth_manager_backend.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/{householdId}/members")
    public Member addMember(
            @PathVariable Long householdId,
            @RequestBody MemberRequest request) {

        return memberService.addMember(householdId, request);
    }
}
