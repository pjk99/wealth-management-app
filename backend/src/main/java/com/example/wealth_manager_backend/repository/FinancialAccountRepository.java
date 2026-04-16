package com.example.wealth_manager_backend.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wealth_manager_backend.entity.FinancialAccount;

public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {
    Optional<FinancialAccount> findByAccountNumberAndAccountTypeAndMemberId(
        String accountNumber,
        String accountType,
        Long memberId
    );
}
