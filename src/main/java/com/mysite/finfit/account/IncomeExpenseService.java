package com.mysite.finfit.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mysite.finfit.user.User;
import com.mysite.finfit.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeExpenseService {

    private final IncomeExpenseRepository incomeExpenseRepository;
    private final UserRepository userRepository;

    // ✅ 전체 조회
    public List<IncomeExpense> getAllIncomeExpenses() {
        return incomeExpenseRepository.findAll();
    }

    // ✅ 사용자별 내역 조회
    public List<IncomeExpense> getIncomeExpensesByUser(User user) {
        return incomeExpenseRepository.findByUserOrderByDateDesc(user);
    }

    // ✅ 저장
    public IncomeExpense saveIncomeExpense(IncomeExpense ie) {
        return incomeExpenseRepository.save(ie);
    }

    // ✅ 삭제
    public void deleteIncomeExpense(Long id) {
        incomeExpenseRepository.deleteById(id);
    }

    // ✅ 사용자 이메일로 조회
    public User getUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // ✅ 날짜 범위 기반 조회 (달력 조회용)
    public List<IncomeExpense> getByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return incomeExpenseRepository.findByUserAndDateBetween(user, startDate, endDate);
    }

    // ✅ 월별 요약 (수입/지출/잔액)
    public Map<String, BigDecimal> getMonthlySummary(User user, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<IncomeExpense> records = incomeExpenseRepository.findByUserIdAndDateBetween(user.getId(), start, end);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (IncomeExpense record : records) {
            if (record.getType() == IncomeExpense.Type.INCOME) {
                totalIncome = totalIncome.add(record.getAmount());
            } else {
                totalExpense = totalExpense.add(record.getAmount());
            }
        }

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("income", totalIncome);
        summary.put("expense", totalExpense);
        summary.put("balance", totalIncome.subtract(totalExpense));
        return summary;
    }

    // ✅ 카테고리별 합계 (월별)
    public Map<String, BigDecimal> getCategorySummary(User user, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<IncomeExpense> records = incomeExpenseRepository.findByUserAndDateBetween(user, start, end);

        return records.stream()
                .collect(Collectors.groupingBy(
                        IncomeExpense::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, IncomeExpense::getAmount, BigDecimal::add)
                ));
    }

    // ✅ 특정 날짜의 수입/지출 내역 조회 (모달창 표시용)
    public List<IncomeExpense> getDailyDetails(User user, LocalDate date) {
        return incomeExpenseRepository.findByUserAndDateBetween(user, date, date);
    }
    
    // ✅ 특정 ID로 내역 조회 (수정용)
    public IncomeExpense findById(Long id) {
        return incomeExpenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("IncomeExpense not found with id: " + id));
    }

    // ✅ 카테고리별 조회
    public List<IncomeExpense> getByCategory(User user, String category) {
        return incomeExpenseRepository.findByUserAndCategory(user, category);
    }
    
    // ✅ 년도 & 월별 필터링
    public List<IncomeExpense> getByYearAndMonth(User user, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return incomeExpenseRepository.findByUserAndDateBetween(user, start, end);
    }

    // ✅ 년도별 필터링
    public List<IncomeExpense> getByYear(User user, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return incomeExpenseRepository.findByUserAndDateBetween(user, start, end);
    }


}
