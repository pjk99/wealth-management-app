package com.example.wealth_manager_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.wealth_manager_backend.dto.AccountSummary;
import com.example.wealth_manager_backend.dto.HouseholdRequest;
import com.example.wealth_manager_backend.dto.HouseholdResponse;
import com.example.wealth_manager_backend.dto.MemberSummary;
import com.example.wealth_manager_backend.entity.Household;
import com.example.wealth_manager_backend.repository.HouseholdRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdRepository householdRepository;

    private HouseholdResponse toHouseholdResponse(Household h) {

        List<MemberSummary> members = h.getMembers()
            .stream()
            .map(m -> new MemberSummary(
                    m.getId(),
                    m.getName(),
                    m.getRelationship()
            ))
            .toList();

        List<AccountSummary> accounts = h.getAccounts()
            .stream()
            .map(a -> new AccountSummary(
                    a.getId(),
                    a.getAccountType(),
                    a.getAccountValue()
            ))
            .toList();

        return new HouseholdResponse(
                h.getId(),
                h.getName(),
                h.getIncome(),
                h.getNetWorth(),
                members,
                accounts
        );
    }

    public Household createHousehold(HouseholdRequest request) {
        Household h = new Household();
        h.setName(request.getName());
        h.setIncome(request.getIncome());
        h.setNetWorth(request.getNetWorth());
        return householdRepository.save(h);
    }

    public HouseholdResponse updateHousehold(Long id, HouseholdRequest request) {
        Household h = householdRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Household not found"));

        if (request.getName() != null) {
            h.setName(request.getName());
        }

        if (request.getIncome() != null) {
            h.setIncome(request.getIncome());
        }

        if (request.getNetWorth() != null) {
            h.setNetWorth(request.getNetWorth());
        }

    Household updated = householdRepository.save(h);
    System.out.println(toHouseholdResponse(updated));

    return toHouseholdResponse(updated);
}

    public HouseholdResponse getHousehold(Long id) {
        Household h = householdRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Household not found"));

        return toHouseholdResponse(h);
    }

    public List<HouseholdResponse> getAll() {
        return householdRepository.findAll()
            .stream()
            .map(this::toHouseholdResponse)
            .toList();
    }
    
}
