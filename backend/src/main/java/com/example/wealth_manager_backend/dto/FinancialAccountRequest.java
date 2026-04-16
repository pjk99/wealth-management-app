package com.example.wealth_manager_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinancialAccountRequest {
    private String accountNumber;
    private String accountType;
    private Double accountValue;
}
