package com.example.wealth_manager_backend.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseholdRequest {
    private String name;
    private Double income;
    private Double netWorth;
}