package com.mysite.finfit.account;

import com.mysite.finfit.account.IncomeExpense;
import com.mysite.finfit.account.IncomeExpenseRepository;
import com.mysite.finfit.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeExpenseService {

    private final IncomeExpenseRepository repository;

    public IncomeExpense save(IncomeExpense incomeExpense) {
        return repository.save(incomeExpense);
    }

    public List<IncomeExpense> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<IncomeExpense> findByUserAndDateRange(User user, LocalDate start, LocalDate end) {
        return repository.findByUserAndDateBetween(user, start, end);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
