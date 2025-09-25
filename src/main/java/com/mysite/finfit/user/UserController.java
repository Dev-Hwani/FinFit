package com.mysite.finfit.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupForm() {
        return "signup"; // signup.html
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("email") String email,
            Model model
    ) {
        try {
            userService.registerUser(username, password, confirmPassword, email);
            return "redirect:/"; // 회원가입 성공 시 홈으로 이동
        } catch (UserException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
            return "signup";
        }
    }
    
    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.html
    }
    
    // 로그인 처리
    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model
    ) {
        try {
            User user = userService.login(email, password);
            // 로그인 성공 시 세션 처리 가능 (예: HttpSession에 저장)
            model.addAttribute("loginUser", user); // 임시
            return "redirect:/"; // 홈으로 이동
        } catch (UserException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }
    }
}
