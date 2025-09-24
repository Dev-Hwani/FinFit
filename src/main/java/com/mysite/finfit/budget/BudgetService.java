package com.mysite.finfit.budget;

import com.mysite.finfit.budget.Budget;
import com.mysite.finfit.budget.BudgetRepository;
import com.mysite.finfit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository repository;

    public Budget save(Budget budget) {
        return repository.save(budget);
    }

    public Optional<Budget> findByUserCategoryMonth(User user, String category, YearMonth month) {
        return repository.findByUserAndCategoryAndMonth(user, category, month);
    }

    public List<Budget> findByUser(User user) {
        return repository.findByUser(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
