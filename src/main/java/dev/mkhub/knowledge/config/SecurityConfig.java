package dev.mkhub.knowledge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/test/**", "/error", "/register", "/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/posts/write", "/posts/edit/**", "/api/comments/**").hasAnyRole("USER", "ADMIN") // ✅ 로그인 필요
                        .requestMatchers("/", "/posts", "/posts/**", "/api/**", "/search/**").permitAll() // ✅ 공개
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/?msg=loginSuccess", true)
                        .failureUrl("/login?msg=error")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/posts?msg=logoutSuccess")
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied") // 🔒 여기 추가
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
