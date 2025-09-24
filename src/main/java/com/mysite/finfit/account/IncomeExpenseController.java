package com.mysite.finfit.account;

import com.mysite.finfit.account.IncomeExpense;
import com.mysite.finfit.account.IncomeExpenseService;
import com.mysite.finfit.user.User;
import com.mysite.finfit.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class IncomeExpenseController {

    private final IncomeExpenseService service;
    private final UserService userService;

    @PostMapping("/{username}")
    public IncomeExpense create(@PathVariable String username, @RequestBody IncomeExpense ie) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ie.assignUser(user);
        return service.save(ie);
    }

    @GetMapping("/{username}")
    public List<IncomeExpense> getAll(@PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return service.findByUser(user);
    }
}
