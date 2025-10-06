package com.mysite.finfit.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.finfit.user.User;

@Repository
public interface IncomeExpenseRepository extends JpaRepository<IncomeExpense, Long> {

    // 사용자별 가계부 조회
    List<IncomeExpense> findByUser(User user);

    // 날짜 범위 기반 조회 (달력용)
    List<IncomeExpense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    // 카테고리 기반 조회
    List<IncomeExpense> findByUserAndCategory(User user, String category);

    // 유형별(INCOME/EXPENSE) 조회
    List<IncomeExpense> findByUserAndType(User user, IncomeExpense.Type type);

    // 정렬 지원(최근 내역부터 조회)
    List<IncomeExpense> findByUserOrderByDateDesc(User user);

    // User ID 기반 조회
    @Query("SELECT i FROM IncomeExpense i WHERE i.user.id = :userId AND i.date BETWEEN :start AND :end ORDER BY i.date DESC")
    List<IncomeExpense> findByUserIdAndDateBetween(@Param("userId") Long userId,
                                                   @Param("start") LocalDate start,
                                                   @Param("end") LocalDate end);

    // 월별 통계(수입, 지출 합계)
    @Query("SELECT SUM(i.amount) FROM IncomeExpense i WHERE i.user.id = :userId AND i.type = :type AND FUNCTION('MONTH', i.date) = :month AND FUNCTION('YEAR', i.date) = :year")
    BigDecimal getTotalByUserIdAndTypeAndMonth(@Param("userId") Long userId,
                                               @Param("type") IncomeExpense.Type type,
                                               @Param("year") int year,
                                               @Param("month") int month);
}
