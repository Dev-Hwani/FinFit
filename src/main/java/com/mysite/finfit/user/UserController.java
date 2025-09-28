package com.mysite.finfit.user;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "birthDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(value = "address", required = false) String address,
            Model model
    ) {
        try {
            userService.registerUser(username, password, confirmPassword, email, phoneNumber, birthDate, address);
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
    
    // 마이페이지
    @GetMapping("/mypage")
    public String myPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName(); // principal에서 로그인한 이메일 가져오기
            User user = userService.getUserByEmail(email); // UserService에서 DB 조회
            model.addAttribute("user", user); // mypage.html에서 user 객체로 접근
        }
        return "mypage"; // mypage.html
    }
    
    // 사용자 정보 수정 화면 (GET)
    @GetMapping("/mypage/edit")
    public String editUserForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getUserByEmail(email);

        model.addAttribute("user", user);
        return "mypage_edit"; // 수정 페이지 템플릿
    }
    
    // 사용자 정보 수정
    @PostMapping("/mypage/edit")
    public String updateUser(
            @RequestParam("username") String username,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @RequestParam(value = "enabled", required = false, defaultValue = "false") boolean enabled,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "birthDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(value = "address", required = false) String address
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        try {
            // 현재 비밀번호 확인
            if (!userService.checkPassword(email, currentPassword)) {
                return "redirect:/mypage/edit?error=password";
            }

            // 새 비밀번호 입력 시 확인 검사
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    return "redirect:/mypage/edit?error=mismatch";
                }
            } else {
                newPassword = null; // 변경하지 않음
            }

            // 사용자 정보 업데이트
            userService.updateUserInfo(email, username, newPassword, enabled, phoneNumber, birthDate, address);

            return "redirect:/mypage?success";

        } catch (Exception e) {
            return "redirect:/mypage/edit?error=unknown";
        }
    }
    
    // 아이디 찾기 폼
    @GetMapping("/find/email")
    public String findEmailForm() {
        return "find_email"; // find_email.html
    }

    // 아이디 찾기 처리
    @PostMapping("/find/email")
    public String findEmail(@RequestParam("username") String username,
                            @RequestParam("phoneNumber") String phoneNumber,
                            Model model) {
        try {
            String email = userService.findEmail(username, phoneNumber);
            model.addAttribute("foundEmail", email);
        } catch (UserException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "find_email";
    }

    // 비밀번호 찾기 폼
    @GetMapping("/find/password")
    public String findPasswordForm() {
        return "find_password"; // find_password.html
    }

    // 비밀번호 찾기 처리
    @PostMapping("/find/password")
    public String findPassword(@RequestParam("email") String email,
                               @RequestParam("phoneNumber") String phoneNumber,
                               Model model) {
        try {
            String tempPassword = userService.resetPassword(email, phoneNumber);
            model.addAttribute("tempPassword", tempPassword);
        } catch (UserException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "find_password";
    }

}
