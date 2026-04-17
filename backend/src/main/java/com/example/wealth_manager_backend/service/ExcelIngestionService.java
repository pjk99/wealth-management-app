package com.example.wealth_manager_backend.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.wealth_manager_backend.dto.ExcelRow;
import com.example.wealth_manager_backend.entity.FinancialAccount;
import com.example.wealth_manager_backend.entity.Household;
import com.example.wealth_manager_backend.entity.Member;
import com.example.wealth_manager_backend.repository.FinancialAccountRepository;
import com.example.wealth_manager_backend.repository.HouseholdRepository;
import com.example.wealth_manager_backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelIngestionService {

    private final HouseholdRepository householdRepository;
    private final MemberRepository memberRepository;
    private final FinancialAccountRepository accountRepository;

    private final ColumnMappingService columnMappingService;

    public void ingest(MultipartFile file) throws Exception {

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Row headerRow = sheet.getRow(0);

        // 1. Extract headers
        Map<String, String> rawHeaders = extractRawHeaders(headerRow);

        // 2. Call AI mapping
        Map<String, String> aiMapping = columnMappingService.mapColumns(rawHeaders.keySet());

        // 3. Convert mapping → target_field → actual header
        Map<String, String> mapping = new HashMap<>();

        if (aiMapping != null && !aiMapping.isEmpty()) {
            for (Map.Entry<String, String> entry : aiMapping.entrySet()) {

                String originalHeader = entry.getKey();     // e.g. "Acct #"
                String targetField = toCamelCase(entry.getValue()); // account_number → accountNumber

                mapping.put(targetField, originalHeader);
            }
        } else {
            // fallback
            mapping = getColumnMappingFallback(rawHeaders);
        }

        // 4. Build column index map
        Map<String, Integer> colMap = mapHeaders(headerRow);

        // 5. Process rows
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            ExcelRow dto = parseRow(row, colMap, mapping);

            processRow(dto);
        }

        workbook.close();
    }

    // HEADER PROCESSING

    private Map<String, String> extractRawHeaders(Row headerRow) {
        Map<String, String> rawHeaders = new HashMap<>();

        for (Cell cell : headerRow) {
            String original = cell.getStringCellValue().trim();
            rawHeaders.put(original.toLowerCase(), original);
        }

        return rawHeaders;
    }

    private Map<String, Integer> mapHeaders(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();

        for (Cell cell : headerRow) {
            map.put(cell.getStringCellValue().trim().toLowerCase(), cell.getColumnIndex());
        }

        return map;
    }

    // PARSING

    private ExcelRow parseRow(Row row,
                             Map<String, Integer> colMap,
                             Map<String, String> mapping) {

        ExcelRow dto = new ExcelRow();

        dto.setHouseholdName(getMapped(row, colMap, mapping, "householdName"));
        dto.setName(getMapped(row, colMap, mapping, "name"));

        dto.setAccountType(getMapped(row, colMap, mapping, "accountType"));
        dto.setAccountNumber(getMapped(row, colMap, mapping, "accountNumber"));
        dto.setCustodian(getMapped(row, colMap, mapping, "custodian"));

        dto.setAnnualIncome(getMapped(row, colMap, mapping, "income"));
        dto.setNetWorth(getMapped(row, colMap, mapping, "netWorth"));

        return dto;
    }

    private String getMapped(Row row,
                             Map<String, Integer> colMap,
                             Map<String, String> mapping,
                             String fieldKey) {

        String header = mapping.get(fieldKey);
        if (header == null) return null;

        Integer idx = colMap.get(header.toLowerCase());
        if (idx == null) return null;

        Cell cell = row.getCell(idx);
        if (cell == null) return null;

        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    // FALLBACK RULES

    private Map<String, String> getColumnMappingFallback(Map<String, String> rawHeaders) {

        Map<String, String> mapping = new HashMap<>();

        for (String header : rawHeaders.keySet()) {

            if (header.contains("household")) mapping.put("householdName", rawHeaders.get(header));
            else if (header.contains("name")) mapping.put("name", rawHeaders.get(header));
            else if (header.contains("account type")) mapping.put("accountType", rawHeaders.get(header));
            else if (header.contains("account")) mapping.put("accountNumber", rawHeaders.get(header));
            else if (header.contains("custodian")) mapping.put("custodian", rawHeaders.get(header));
            else if (header.contains("income")) mapping.put("income", rawHeaders.get(header));
            else if (header.contains("net worth")) mapping.put("netWorth", rawHeaders.get(header));
        }

        return mapping;
    }

    // UTIL

    private String toCamelCase(String snake) {
        if (snake == null) return null;

        String[] parts = snake.split("_");
        StringBuilder result = new StringBuilder(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            result.append(parts[i].substring(0,1).toUpperCase())
                  .append(parts[i].substring(1));
        }

        return result.toString();
    }

    // DB PROCESSING

    private void processRow(ExcelRow dto) {

        if (dto.getHouseholdName() == null || dto.getName() == null) return;

        // Household
        Household household = householdRepository
                .findByName(dto.getHouseholdName())
                .orElseGet(() -> {
                    Household h = new Household();
                    h.setName(dto.getHouseholdName());
                    h.setIncome(Double.parseDouble(dto.getAnnualIncome()));
                    h.setNetWorth(Double.parseDouble(dto.getNetWorth()));

                    return householdRepository.save(h);
                });

        // Member
        Member member = memberRepository
                .findByNameAndHouseholdId(dto.getName(), household.getId())
                .orElseGet(() -> {
                    Member m = new Member();
                    m.setName(dto.getName());
                    m.setHousehold(household);
                    return memberRepository.save(m);
                });

        // Account
        FinancialAccount account = accountRepository
                .findByAccountNumberAndAccountTypeAndMemberId(
                        dto.getAccountNumber(),
                        dto.getAccountType(),
                        member.getId()
                )
                .orElseGet(() -> {
                    FinancialAccount a = new FinancialAccount();
                    a.setAccountNumber(dto.getAccountNumber());
                    a.setAccountType(dto.getAccountType());
                    a.setMember(member);
                    a.setHousehold(household);
                    return a;
                });

        account.setCustodian(dto.getCustodian());

        accountRepository.save(account);
    }
}