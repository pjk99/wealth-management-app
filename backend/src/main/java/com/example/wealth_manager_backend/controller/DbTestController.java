package com.example.wealth_manager_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wealth_manager_backend.entity.TestEntity;
import com.example.wealth_manager_backend.repository.TestRepository;

@RestController
@RequestMapping("/api/db")
public class DbTestController {

    @Autowired
    private TestRepository repo;

    @GetMapping("/test")
    public String test() {
        TestEntity t = new TestEntity();
        t.setName("hello");
        repo.save(t);
        return "Saved to DB";
    }
}
