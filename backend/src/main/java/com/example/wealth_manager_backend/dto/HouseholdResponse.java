package com.example.wealth_manager_backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HouseholdResponse {

    private Long id;
    private String name;
    private Double income;
    private Double netWorth;

    private List<MemberSummary> members;
}
