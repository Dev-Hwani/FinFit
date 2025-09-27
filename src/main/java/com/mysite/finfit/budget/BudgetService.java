package com.mysite.finfit.budget;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mysite.finfit.user.User;
import com.mysite.finfit.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    // 전체 Budget 조회
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }
    
    // 특정 사용자의 예산 목록 조회
    public List<Budget> getBudgetsByUser(User user) {
        return budgetRepository.findByUser(user);
    }

    // Budget 저장
    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    // Budget 삭제
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
    
 // 사용자 이메일로 User 조회
    public User getUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
