package com.example.wealth_manager_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountSummary {
    private Long id;
    private String accountType;
    private Double accountValue;
}