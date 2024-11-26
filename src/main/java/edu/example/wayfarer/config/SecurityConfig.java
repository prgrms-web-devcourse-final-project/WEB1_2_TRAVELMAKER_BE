package edu.example.wayfarer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//임시 config 파일입니다.

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((auth) -> auth.disable()); // CSRF 비활성화
        http.formLogin((auth) -> auth.disable()); // Form Login 비활성화
        http.httpBasic((auth) -> auth.disable()); // HTTP Basic 비활성화
        http
                .authorizeRequests()
                .requestMatchers("/**").permitAll();

        return http.build();
    }

}
