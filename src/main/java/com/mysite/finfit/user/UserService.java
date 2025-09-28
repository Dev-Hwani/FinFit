package com.mysite.finfit.user;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,}$");

    // 회원가입
    public User registerUser(String username, String password, String confirmPassword, String email, String phoneNumber, LocalDate birthDate, String address) {
        validateUsername(username);
        validatePassword(password);
        validatePasswordMatch(password, confirmPassword);
        validateEmail(email);

        checkDuplicateUsername(username);
        checkDuplicateEmail(email);

        String encodedPassword = passwordEncoder.encode(password);

        // 첫 번째 가입자라면 ADMIN, 나머지는 USER
        UserRole role = userRepository.count() == 0 ? UserRole.ADMIN : UserRole.USER;

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .enabled(true)
                .role(role) // User 엔티티에 UserRole role 필드가 있어야 함
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .address(address)
                .build();

        return userRepository.save(user);
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));
    }

    // --- 검증 메서드 ---
    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new UserException("사용자 이름을 입력해주세요.");
        }
        if (username.length() < 3) {
            throw new UserException("사용자 이름은 최소 3자 이상이어야 합니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new UserException("비밀번호는 최소 8자, 대문자/소문자/숫자/특수문자를 포함해야 합니다.");
        }
    }

    private void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new UserException("유효한 이메일 주소를 입력해주세요.");
        }
    }

    private void checkDuplicateUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserException("이미 존재하는 사용자 이름입니다.");
        }
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException("이미 존재하는 이메일입니다.");
        }
    }
    
    /**
     * 이메일과 비밀번호를 이용해 로그인 시도
     * @param email 사용자 이메일
     * @param rawPassword 입력한 비밀번호
     * @return 로그인 성공 시 User 객체 반환
     * @throws UserException 로그인 실패 시 예외 발생
     */
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("등록되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }

        if (!user.isEnabled()) {
            throw new UserException("사용할 수 없는 계정입니다.");
        }

        return user;
    }
    
    // 이메일로 User 조회
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
    
    // 현재 비밀번호 확인
    public boolean checkPassword(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // 사용자 정보 업데이트
    public void updateUserInfo(String email, String username, String newPassword, boolean enabled, String phoneNumber, LocalDate birthDate, String address) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));

        user.setUsername(username);
        user.setEnabled(enabled);
        user.setPhoneNumber(phoneNumber);
        user.setBirthDate(birthDate);
        user.setAddress(address);

        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
    }
    
    // 이메일 찾기
    public String findEmail(String username, String phoneNumber) {
        User user = userRepository.findByUsernameAndPhoneNumber(username, phoneNumber)
                .orElseThrow(() -> new UserException("일치하는 사용자가 없습니다."));
        return user.getEmail();
    }

    // 비밀번호 초기화 (임시 비밀번호 생성)
    public String resetPassword(String email, String phoneNumber) {
        User user = userRepository.findByEmailAndPhoneNumber(email, phoneNumber)
                .orElseThrow(() -> new UserException("일치하는 사용자가 없습니다."));

        String tempPassword = generateTempPassword(); // 임시 비밀번호 생성
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
        return tempPassword;
    }

    // 임시 비밀번호 생성
    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 임시 비밀번호
    }

}
