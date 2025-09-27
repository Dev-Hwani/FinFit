package com.mysite.finfit.budget;

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
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("/budget")
    public String budgetList(Model model) {
        // ✅ 현재 로그인한 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = budgetService.getUserByUsername(email);

        // ✅ 로그인한 사용자의 예산만 조회
        List<Budget> budgets = budgetService.getBudgetsByUser(user);
        model.addAttribute("budgets", budgets);

        return "budget"; // budget.html
    }

    @PostMapping("/budget/save")
    public String saveBudget(
            @RequestParam("category") String category,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        // ✅ 현재 로그인한 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = budgetService.getUserByUsername(email);

        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .amount(amount)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        budgetService.saveBudget(budget);

        return "redirect:/budget";
    }
}
