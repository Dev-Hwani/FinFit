package com.mysite.finfit.account;

import com.mysite.finfit.account.IncomeExpense;
import com.mysite.finfit.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
    List<IncomeExpense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
    List<IncomeExpense> findByUser(User user);
}
