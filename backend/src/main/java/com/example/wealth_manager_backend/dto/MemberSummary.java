package com.example.wealth_manager_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MemberSummary {
    private Long id;
    private String name;
    private String relationship;

    private List<AccountSummary> accounts;
}