package com.example.wealth_manager_backend.dto;

import lombok.Data;

@Data
public class ExcelRow {
    private String householdName;

    private String name;

    private String accountType;
    private String accountNumber;
    private String custodian;

    private String email;
    private String phone;
    private String address;

    private String ssn;
    private String dob;

    private String occupation;

    private String netWorth;
    private String totalNetWorth;
    private String annualIncome;

    private String riskTolerance;
    private String timeHorizon;
}