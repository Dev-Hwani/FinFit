package com.mysite.finfit.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
}
