package com.example.wealth_manager_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.wealth_manager_backend.service.ExcelIngestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelIngestionService excelService;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws Exception {
        excelService.ingest(file);

        return "Excel processed successfully";
    }
}
