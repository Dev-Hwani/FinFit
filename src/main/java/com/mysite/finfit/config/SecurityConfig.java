package com.mysite.finfit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 비밀번호 암호화 Bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CSRF 비활성화 (개발용)
            .csrf(csrf -> csrf.disable())

            // URL별 접근 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 로그인 없이 접근 가능한 URL
                .requestMatchers("/", "/home", "/user/login", "/user/signup").permitAll()
                // 정적 리소스 허용
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // 나머지 요청은 인증 필요
                .anyRequest().authenticated()
            )

            // 로그인 설정
            .formLogin(form -> form
                .loginPage("/user/login")        // 커스텀 로그인 페이지
                .defaultSuccessUrl("/", true)    // 로그인 성공 후 홈페이지
                .permitAll()
            )

            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/user/logout")       // 로그아웃 처리 URL
                .logoutSuccessUrl("/")           // 로그아웃 후 이동할 페이지
                .invalidateHttpSession(true)     // 세션 무효화
                .deleteCookies("JSESSIONID")     // 쿠키 삭제
                .permitAll()
            );

        return http.build();
    }
}
