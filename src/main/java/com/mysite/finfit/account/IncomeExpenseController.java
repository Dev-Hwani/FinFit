package com.mysite.finfit.account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.finfit.user.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IncomeExpenseController {

    private final IncomeExpenseService incomeExpenseService;

    // ✅ 가계부 메인 페이지 (조회)
    @GetMapping("/incomeexpense")
    public String incomeExpenseList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = incomeExpenseService.getUserByUsername(email);

        List<IncomeExpense> list = incomeExpenseService.getIncomeExpensesByUser(user);
        Map<String, BigDecimal> summary = incomeExpenseService.getMonthlySummary(
                user, LocalDate.now().getYear(), LocalDate.now().getMonthValue()
        );

        model.addAttribute("incomeExpenses", list);
        model.addAttribute("summary", summary);

        return "incomeexpense";
    }

    // ✅ 등록
    @PostMapping("/incomeexpense/save")
    public String saveIncomeExpense(
            @RequestParam("type") IncomeExpense.Type type,
            @RequestParam("category") String category,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("date") LocalDate date,
            @RequestParam(value = "description", required = false) String description
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = incomeExpenseService.getUserByUsername(email);

        IncomeExpense ie = IncomeExpense.builder()
                .user(user)
                .type(type)
                .category(category)
                .amount(amount)
                .date(date)
                .description(description)
                .build();

        incomeExpenseService.saveIncomeExpense(ie);
        return "redirect:/incomeexpense";
    }

    // ✅ 수정
    @PostMapping("/incomeexpense/update")
    public String updateIncomeExpense(
            @RequestParam("id") Long id,
            @RequestParam("category") String category,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("date") LocalDate date,
            @RequestParam(value = "description", required = false) String description
    ) {
        IncomeExpense ie = incomeExpenseService.findById(id); // ✅ 서비스에 추가 필요
        ie.setCategory(category);
        ie.setAmount(amount);
        ie.setDate(date);
        ie.setDescription(description);
        incomeExpenseService.saveIncomeExpense(ie);

        return "redirect:/incomeexpense";
    }

    // ✅ 삭제
    @PostMapping("/incomeexpense/delete")
    public String deleteIncomeExpense(@RequestParam("id") Long id) {
        incomeExpenseService.deleteIncomeExpense(id);
        return "redirect:/incomeexpense";
    }

 // ✅ 날짜·카테고리·년도·월 필터링
    @GetMapping("/incomeexpense/filter")
    public String filterIncomeExpense(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "month", required = false) Integer month,
            @RequestParam(name = "date", required = false) String date,
            Model model
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = incomeExpenseService.getUserByUsername(email);

        List<IncomeExpense> filteredList;

        if (category != null && !category.isEmpty()) {
            // ✅ 카테고리별 필터링
            filteredList = incomeExpenseService.getByCategory(user, category);

        } else if (year != null && month != null) {
            // ✅ 년/월 필터링
            filteredList = incomeExpenseService.getByYearAndMonth(user, year, month);

        } else if (year != null) {
            // ✅ 년도만 필터링
            filteredList = incomeExpenseService.getByYear(user, year);

        } else if (date != null && !date.isEmpty()) {
            // ✅ 특정 날짜 필터링
            LocalDate selectedDate = LocalDate.parse(date);
            filteredList = incomeExpenseService.getDailyDetails(user, selectedDate);

        } else {
            // ✅ 전체
            filteredList = incomeExpenseService.getIncomeExpensesByUser(user);
        }

        Map<String, BigDecimal> summary = incomeExpenseService.getMonthlySummary(
                user,
                (year != null ? year : LocalDate.now().getYear()),
                (month != null ? month : LocalDate.now().getMonthValue())
        );

        model.addAttribute("incomeExpenses", filteredList);
        model.addAttribute("summary", summary);
        return "incomeexpense";
    }

    
    // ✅ 달력 이벤트 JSON 제공
    @GetMapping("/incomeexpense/events")
    @ResponseBody
    public List<Map<String, String>> getEvents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = incomeExpenseService.getUserByUsername(auth.getName());
        List<IncomeExpense> list = incomeExpenseService.getIncomeExpensesByUser(user);
        return list.stream().map(ie -> Map.of(
                "title", ie.getType() + " " + ie.getAmount(),
                "start", ie.getDate().toString(),
                "color", ie.getType() == IncomeExpense.Type.INCOME ? "green" : "red",
                "category", ie.getCategory(),
                "description", ie.getDescription()
        )).toList();
    }

}
