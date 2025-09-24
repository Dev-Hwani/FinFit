package com.mysite.finfit.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // -------------------------------
    // 웹 화면 반환
    // -------------------------------

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "user/login"; // templates/user/login.html
    }

    // 회원가입 페이지 (URL 변경: /user/signup)
    @GetMapping("/signup")
    public String signupForm() {
        return "user/signup"; // templates/user/signup.html
    }

    // -------------------------------
    // REST API용
    // -------------------------------
    @RestController
    @RequestMapping("/api/users")
    @RequiredArgsConstructor
    static class UserApiController {

        private final UserService userService;

        // 회원가입 API (URL 변경: /api/users/signup)
        @PostMapping("/signup")
        public User register(@RequestBody User user) {
            return userService.register(user);
        }

        @GetMapping("/{username}")
        public User getUser(@PathVariable String username) {
            return userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
    }
}
