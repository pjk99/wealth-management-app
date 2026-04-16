package com.example.wealth_manager_backend.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

    public void ingest(MultipartFile file) throws Exception {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Row header = sheet.getRow(0);
        Map<String, Integer> colMap = mapHeaders(header);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            ExcelRow dto = parseRow(row, colMap);

            processRow(dto);
        }

        workbook.close();
            
        } catch (Exception e) {
            System.out.println("Error: Could not process excel data \n" + e);
        }

        
    }

    private Map<String, Integer> mapHeaders(Row header) {
        Map<String, Integer> map = new HashMap<>();

        for (Cell cell : header) {
            map.put(cell.getStringCellValue().trim().toLowerCase(), cell.getColumnIndex());
        }

        return map;
    }

    private ExcelRow parseRow(Row row, Map<String, Integer> colMap) {

    ExcelRow dto = new ExcelRow();

    dto.setHouseholdName(get(row, colMap, "household name"));

    dto.setName(get(row, colMap, "name"));

    dto.setAccountType(get(row, colMap, "account type"));
    dto.setAccountNumber(get(row, colMap, "account number"));
    dto.setCustodian(get(row, colMap, "custodian"));

    dto.setEmail(get(row, colMap, "email"));
    dto.setPhone(get(row, colMap, "phone #"));

    dto.setAddress(get(row, colMap, "address"));
    dto.setSsn(get(row, colMap, "ssn#"));
    dto.setDob(get(row, colMap, "dob"));

    dto.setOccupation(get(row, colMap, "occupation"));

    dto.setNetWorth(get(row, colMap, "estimated total net worth"));
    dto.setAnnualIncome(get(row, colMap, "annual income"));

    return dto;
}

    private String get(Row row, Map<String, Integer> colMap, String key) {
        Integer idx = colMap.get(key.toLowerCase());
        if (idx == null) return null;

        Cell cell = row.getCell(idx);
        if (cell == null) return null;

        return cell.toString().trim();
    }

    private void processRow(ExcelRow dto) {

        // 1. Household
        Household household = householdRepository
                .findByName(dto.getHouseholdName())
                .orElseGet(() -> {
                    Household h = new Household();
                    h.setName(dto.getHouseholdName());
                    return householdRepository.save(h);
                });

        // 2. Member
        Member member = memberRepository
                .findByNameAndHouseholdId(dto.getName(), household.getId())
                .orElseGet(() -> {
                    Member m = new Member();
                    m.setName(dto.getName());
                    m.setHousehold(household);
                    return memberRepository.save(m);
                });

        // 3. Account
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