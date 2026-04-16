package com.example.wealth_manager_backend.service;

import org.springframework.stereotype.Service;

import com.example.wealth_manager_backend.dto.FinancialAccountRequest;
import com.example.wealth_manager_backend.entity.FinancialAccount;
import com.example.wealth_manager_backend.entity.Household;
import com.example.wealth_manager_backend.entity.Member;
import com.example.wealth_manager_backend.repository.FinancialAccountRepository;
import com.example.wealth_manager_backend.repository.HouseholdRepository;
import com.example.wealth_manager_backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialAccountService {

    private final HouseholdRepository householdRepository;
    private final MemberRepository memberRepository;
    private final FinancialAccountRepository accountRepository;

    public FinancialAccount addAccount(Long memberId, FinancialAccountRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Household household = member.getHousehold();

        FinancialAccount account = new FinancialAccount();
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setAccountValue(request.getAccountValue());
        account.setCustodian(request.getCustodian());
        account.setOwnershipPercentage(request.getOwnershipPercentage());

        account.setMember(member);
        account.setHousehold(household);

        member.getAccounts().add(account);
        // household.getAccounts().add(account);

        return accountRepository.save(account);
    }
}