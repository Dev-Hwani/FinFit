package com.mysite.finfit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // 루트 URL -> 홈페이지로 이동
    @GetMapping("/")
    public String home() {
        return "home"; // home.html 템플릿 반환
    }

    // 가계부 페이지 이동
    @GetMapping("/incomeexpense")
    public String incomeExpense() {
        return "incomeexpense"; // incomeexpense.html
    }

    // 예산 관리 페이지 이동
    @GetMapping("/budget")
    public String budget() {
        return "budget"; // budget.html
    }
}
