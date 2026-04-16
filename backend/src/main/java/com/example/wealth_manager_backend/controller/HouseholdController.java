package com.example.wealth_manager_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wealth_manager_backend.dto.HouseholdRequest;
import com.example.wealth_manager_backend.dto.HouseholdResponse;
import com.example.wealth_manager_backend.entity.Household;
import com.example.wealth_manager_backend.service.HouseholdService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
public class HouseholdController {

    private final HouseholdService service;

    @PostMapping
    public Household create(@RequestBody HouseholdRequest request) {
        return service.createHousehold(request);
    }

    @PatchMapping("/{id}")
    public HouseholdResponse updateHousehold(
            @PathVariable Long id,
            @RequestBody HouseholdRequest request) {

        return service.updateHousehold(id, request);
    }

    @GetMapping
    public List<HouseholdResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public HouseholdResponse getHousehold(@PathVariable Long id) {
        return service.getHousehold(id);
    }
}