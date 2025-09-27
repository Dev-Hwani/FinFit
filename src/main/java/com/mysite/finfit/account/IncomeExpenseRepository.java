package com.mysite.finfit.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.finfit.user.User;

@Repository
public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {
	List<IncomeExpense> findByUser(User user); // 사용자별 가계부 조회
}
