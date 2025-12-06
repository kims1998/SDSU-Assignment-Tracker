package com.sdsu.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. Disable CSRF protection for API calls
        http.csrf(AbstractHttpConfigurer::disable)
                // 2. Configure endpoint authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow POST requests to /api/auth/login and /api/users (Sign Up) without authentication
                        .requestMatchers("/api/auth/login", "/api/users").permitAll()
                        .requestMatchers("/api/calendar-events").permitAll()
                        // All other requests must be authenticated (protected)
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
