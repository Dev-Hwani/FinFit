package com.mysite.finfit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.mysite.finfit.account.IncomeExpense;
import com.mysite.finfit.account.IncomeExpenseRepository;
import com.mysite.finfit.budget.Budget;
import com.mysite.finfit.budget.BudgetRepository;
import com.mysite.finfit.user.User;
import com.mysite.finfit.user.UserRepository;

@SpringBootTest
class FinfitApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncomeExpenseRepository incomeExpenseRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    @Rollback(false)
    void initTestData() {
        // 1. 기존 데이터 삭제 (Budget → IncomeExpense → User 순서)
        budgetRepository.deleteAll();
        incomeExpenseRepository.deleteAll();
        userRepository.deleteAll();

        // 2. User 생성
        User user1 = User.builder()
                .username("testuser1")
                .password("password123")
                .email("testuser1@example.com")
                .enabled(true)
                .build();

        User user2 = User.builder()
                .username("testuser2")
                .password("password456")
                .email("testuser2@example.com")
                .enabled(true)
                .build();

        userRepository.saveAll(List.of(user1, user2));

        // 3. IncomeExpense 생성
        IncomeExpense ie1 = IncomeExpense.builder()
                .user(user1)
                .type(IncomeExpense.Type.INCOME)
                .category("월급")
                .amount(new BigDecimal("3000000"))
                .date(LocalDate.of(2025, 9, 1))
                .description("9월 월급")
                .build();

        IncomeExpense ie2 = IncomeExpense.builder()
                .user(user1)
                .type(IncomeExpense.Type.EXPENSE)
                .category("식비")
                .amount(new BigDecimal("500000"))
                .date(LocalDate.of(2025, 9, 2))
                .description("식비 지출")
                .build();

        IncomeExpense ie3 = IncomeExpense.builder()
                .user(user2)
                .type(IncomeExpense.Type.INCOME)
                .category("프리랜스")
                .amount(new BigDecimal("1500000"))
                .date(LocalDate.of(2025, 9, 5))
                .description("프로젝트 수익")
                .build();

        incomeExpenseRepository.saveAll(List.of(ie1, ie2, ie3));

        // 4. Budget 생성
        Budget b1 = Budget.builder()
                .user(user1)
                .category("식비")
                .amount(new BigDecimal("500000"))
                .startDate(LocalDate.of(2025, 9, 1))
                .endDate(LocalDate.of(2025, 9, 30))
                .build();

        Budget b2 = Budget.builder()
                .user(user1)
                .category("교통비")
                .amount(new BigDecimal("200000"))
                .startDate(LocalDate.of(2025, 9, 1))
                .endDate(LocalDate.of(2025, 9, 30))
                .build();

        Budget b3 = Budget.builder()
                .user(user2)
                .category("식비")
                .amount(new BigDecimal("400000"))
                .startDate(LocalDate.of(2025, 9, 1))
                .endDate(LocalDate.of(2025, 9, 30))
                .build();

        Budget b4 = Budget.builder()
                .user(user2)
                .category("교통비")
                .amount(new BigDecimal("150000"))
                .startDate(LocalDate.of(2025, 9, 1))
                .endDate(LocalDate.of(2025, 9, 30))
                .build();

        budgetRepository.saveAll(List.of(b1, b2, b3, b4));

        System.out.println("✅ 모든 테스트 데이터 저장 완료!");
    }
}
