package com.example.wealth_manager_backend.dto;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberRequest {
    private String name;
    private String relationship;
    private LocalDate dateOfBirth;
}