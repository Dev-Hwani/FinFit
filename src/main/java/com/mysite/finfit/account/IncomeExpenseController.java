package com.mysite.finfit.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.finfit.user.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IncomeExpenseController {

    private final IncomeExpenseService incomeExpenseService;

    @GetMapping("/incomeexpense")
    public String incomeExpenseList(Model model) {
        List<IncomeExpense> list = incomeExpenseService.getAllIncomeExpenses();
        model.addAttribute("incomeExpenses", list);
        return "incomeexpense"; // incomeexpense.html
    }

    @PostMapping("/incomeexpense/save")
    public String saveIncomeExpense(
            @RequestParam("type") IncomeExpense.Type type,
            @RequestParam("category") String category,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("date") LocalDate date,
            @RequestParam(value = "description", required = false) String description
    ) {
        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); 

        // 1. 서비스 통해 사용자 조회
        User user = incomeExpenseService.getUserByUsername(email);

        // 2. 데이터 생성
        IncomeExpense ie = IncomeExpense.builder()
                .user(user)
                .type(type)
                .category(category)
                .amount(amount)
                .date(date)
                .description(description)
                .build();

        // 3. 서비스 통해 저장
        incomeExpenseService.saveIncomeExpense(ie);

        return "redirect:/incomeexpense";
    }
}
