package com.example.wealth_manager_backend.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wealth_manager_backend.dto.FinancialAccountRequest;
import com.example.wealth_manager_backend.entity.FinancialAccount;
import com.example.wealth_manager_backend.service.FinancialAccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
public class FinancialAccountController {

    private final FinancialAccountService accountService;

    @PostMapping("/{householdId}/accounts")
    public FinancialAccount addAccount(
            @PathVariable Long householdId,
            @RequestBody FinancialAccountRequest request) {

        return accountService.addAccount(householdId, request);
    }
}