package com.example.wealth_manager_backend.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wealth_manager_backend.entity.Household;

public interface HouseholdRepository extends JpaRepository<Household, Long> {
    Optional<Household> findByName(String name);
}

