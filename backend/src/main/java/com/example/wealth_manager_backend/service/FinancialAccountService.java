package com.example.wealth_manager_backend.service;

import org.springframework.stereotype.Service;

import com.example.wealth_manager_backend.dto.FinancialAccountRequest;
import com.example.wealth_manager_backend.entity.FinancialAccount;
import com.example.wealth_manager_backend.entity.Household;
import com.example.wealth_manager_backend.repository.FinancialAccountRepository;
import com.example.wealth_manager_backend.repository.HouseholdRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialAccountService {

    private final HouseholdRepository householdRepository;
    private final FinancialAccountRepository accountRepository;

    public FinancialAccount addAccount(Long householdId, FinancialAccountRequest request) {

        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new RuntimeException("Household not found"));

        FinancialAccount account = new FinancialAccount();
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setAccountValue(request.getAccountValue());

        // Important
        account.setHousehold(household);
        household.getAccounts().add(account);

        return accountRepository.save(account);
    }
}