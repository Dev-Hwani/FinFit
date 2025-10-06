package com.mysite.finfit.user;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import com.mysite.finfit.account.IncomeExpense;
import com.mysite.finfit.budget.Budget;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 기본 생성자 직접 사용 제한
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true; // 계정 활성화 여부
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(length = 20)
    private String phoneNumber; // 전화번호

    @Column
    private LocalDate birthDate; // 생년월일

    @Column(length = 200)
    private String address; // 주소
    
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncomeExpense> incomeExpenses = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Budget> budgets = new ArrayList<>();
    
    // ================================
    // 연관관계 편의 메서드
    // ================================
    public void addIncomeExpense(IncomeExpense ie) {
        incomeExpenses.add(ie);
        ie.setUser(this);
    }

    public void removeIncomeExpense(IncomeExpense ie) {
        incomeExpenses.remove(ie);
        ie.setUser(null);
    }

    public void addBudget(Budget budget) {
        budgets.add(budget);
        budget.setUser(this);
    }

    public void removeBudget(Budget budget) {
        budgets.remove(budget);
        budget.setUser(null);
    }

    // 비밀번호 변경 편의 메서드
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
