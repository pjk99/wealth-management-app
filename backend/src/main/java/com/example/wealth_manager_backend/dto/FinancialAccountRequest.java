package com.example.wealth_manager_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinancialAccountRequest {
    private String accountNumber;
    private String accountType;
    private String custodian;
    private Double accountValue;
    private Double ownershipPercentage;
}
