package com.example.wealth_manager_backend.service;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColumnMappingService {

    private final AiClient aiClient;

    public Map<String, String> mapColumns(Set<String> headers) {
        return aiClient.mapHeaders(headers);
    }
}