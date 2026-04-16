package com.example.wealth_manager_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wealth_manager_backend.entity.FinancialAccount;

public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {}

