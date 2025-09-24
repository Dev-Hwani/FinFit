package com.mysite.finfit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // 루트 URL -> 홈페이지로 이동
    @GetMapping("/")
    public String home() {
        return "home"; // home.html
    }
}
