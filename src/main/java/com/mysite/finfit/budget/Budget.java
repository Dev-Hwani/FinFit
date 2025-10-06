package com.mysite.finfit.budget;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mysite.finfit.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "budget",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "category", "start_date", "end_date"})})
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // 예산 금액 업데이트 편의 메서드
    public void updateAmount(BigDecimal newAmount) {
        this.amount = newAmount;
    }

    // 기간 업데이트 편의 메서드
    public void updatePeriod(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
    }
    
    public void assignUser(User user) {
        this.user = user;
        if (!user.getBudgets().contains(this)) {
            user.getBudgets().add(this);
        }
    }

}
