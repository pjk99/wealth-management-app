package com.example.wealth_manager_backend.service;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.wealth_manager_backend.entity.FinancialAccount;
import com.example.wealth_manager_backend.entity.Household;
import com.example.wealth_manager_backend.entity.Member;
import com.example.wealth_manager_backend.repository.FinancialAccountRepository;
import com.example.wealth_manager_backend.repository.HouseholdRepository;
import com.example.wealth_manager_backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final HouseholdRepository householdRepository;
    private final MemberRepository memberRepository;
    private final FinancialAccountRepository accountRepository;

    public void processExcel(MultipartFile file) {

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0); // single sheet assumed for Phase 1

            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                String householdName = getCell(row, 1);
                String memberName = getCell(row, 2);
                String accountType = getCell(row, 3);
                String netWorthStr = getCell(row, 4);

                if (householdName == null || householdName.isEmpty()) {
                    continue;
                }

                Double netWorth = parseDouble(netWorthStr);

                processRow(householdName, memberName, accountType, netWorth);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file", e);
        }
    }

    private void processRow(String householdName,
                            String memberName,
                            String accountType,
                            Double netWorth) {

        // 1. HOUSEHOLD (create if not exists)
        Household household = householdRepository.findByName(householdName)
                .orElseGet(() -> {
                    Household h = new Household();
                    h.setName(householdName);
                    h.setIncome(0.0);
                    h.setNetWorth(0.0);
                    return householdRepository.save(h);
                });

        // 2. MEMBER (avoid duplicates by name + household)
        Member member = memberRepository
                .findByNameAndHouseholdId(memberName, household.getId())
                .orElseGet(() -> {
                    Member m = new Member();
                    m.setName(memberName);
                    m.setHousehold(household);
                    return memberRepository.save(m);
                });

        memberRepository.save(member);

        // 3. ACCOUNT (Phase 1 simplified)
        FinancialAccount account = new FinancialAccount();
        account.setAccountType(accountType);
        account.setAccountValue(netWorth);
        account.setHousehold(household);

        accountRepository.save(account);

        // 4. Update household net worth (simple aggregation)
        household.setNetWorth(
                (household.getNetWorth() == null ? 0 : household.getNetWorth())
                        + netWorth
        );

        householdRepository.save(household);
    }

    // ---------- helpers ----------

    private String getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell == null ? null : cell.toString().trim();
    }

    private Double parseDouble(String value) {
        try {
            return value == null ? 0.0 : Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
