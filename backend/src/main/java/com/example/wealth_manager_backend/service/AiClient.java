package com.example.wealth_manager_backend.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class AiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String AI_URL = "http://localhost:8000/map-headers";

    public Map<String, String> mapHeaders(Set<String> headers) {

        Map<String, Object> request = new HashMap<>();
        request.put("headers", headers);

        Map response = restTemplate.postForObject(AI_URL, request, Map.class);

        return (Map<String, String>) response.get("mapped_headers");
    }
}