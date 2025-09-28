package com.mysite.finfit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                		new AntPathRequestMatcher("/"),        // 홈 페이지 허용
                        new AntPathRequestMatcher("/signup"),  // 회원가입 허용
                        new AntPathRequestMatcher("/login"),   // 로그인 허용
                        new AntPathRequestMatcher("/find/email"),
                        new AntPathRequestMatcher("/find/password"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**"),
                        new AntPathRequestMatcher("/images/**")
                ).permitAll()  // 회원가입, 정적 리소스는 허용
                .anyRequest().authenticated() // 나머지는 인증 필요
            )
            .formLogin((form) -> form
                .loginPage("/login")              // 로그인 페이지(GET)
                .loginProcessingUrl("/login")     // 로그인 처리(POST)
                .usernameParameter("email")       // 로그인 폼의 name 속성과 맞추기
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)     // 성공 시 홈으로 이동
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/") // 로그아웃 후 이동
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .csrf(csrf -> csrf.disable()); // 개발 단계에서만 disable

        return http.build();
    }
}
