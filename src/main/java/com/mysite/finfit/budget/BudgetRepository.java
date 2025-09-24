package com.mysite.finfit.budget;

import com.mysite.finfit.budget.Budget;
import com.mysite.finfit.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserAndCategoryAndMonth(User user, String category, YearMonth month);
    List<Budget> findByUser(User user);
}
