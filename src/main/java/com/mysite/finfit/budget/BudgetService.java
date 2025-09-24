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

    // Budget 저장
    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    // Budget 삭제
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
    
    // 사용자 조회
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
    }
}
