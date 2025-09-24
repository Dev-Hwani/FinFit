package com.mysite.finfit.budget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        List<Budget> budgets = budgetService.getAllBudgets();
        model.addAttribute("budgets", budgets);
        return "budget"; // budget.html
    }
    
    @PostMapping("/budget/save")
    public String saveBudget(
            @RequestParam("username") String username,
            @RequestParam("category") String category,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        User user = budgetService.getUserByUsername(username);

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
