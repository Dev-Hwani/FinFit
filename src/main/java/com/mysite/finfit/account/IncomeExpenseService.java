package com.mysite.finfit.account;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mysite.finfit.user.User;
import com.mysite.finfit.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeExpenseService {

    private final IncomeExpenseRepository incomeExpenseRepository;
    private final UserRepository userRepository;

    // 전체 IncomeExpense 조회
    public List<IncomeExpense> getAllIncomeExpenses() {
        return incomeExpenseRepository.findAll();
    }
    
    public List<IncomeExpense> getIncomeExpensesByUser(User user) {
        return incomeExpenseRepository.findByUser(user);
    }


    // 저장
    public IncomeExpense saveIncomeExpense(IncomeExpense ie) {
        return incomeExpenseRepository.save(ie);
    }

    // 삭제
    public void deleteIncomeExpense(Long id) {
        incomeExpenseRepository.deleteById(id);
    }

    // 사용자 이메일로 User 조회 (username → email)
    public User getUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

}
