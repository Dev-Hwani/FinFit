package com.mysite.finfit.budget;

import com.mysite.finfit.budget.Budget;
import com.mysite.finfit.budget.BudgetService;
import com.mysite.finfit.user.User;
import com.mysite.finfit.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;
    private final UserService userService;

    @PostMapping("/{username}")
    public Budget create(@PathVariable String username, @RequestBody Budget budget) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        budget.setUser(user);
        return service.save(budget);
    }

    @GetMapping("/{username}")
    public List<Budget> getAll(@PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return service.findByUser(user);
    }
}
