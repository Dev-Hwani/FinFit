package com.mysite.finfit.budget;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysite.finfit.user.User;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
	List<Budget> findByUser(User user);
}
