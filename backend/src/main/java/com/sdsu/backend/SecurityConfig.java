package com.sdsu.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disable CSRF for test endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/hello").permitAll() // allow /hello without login
                .anyRequest().authenticated()          // all other endpoints require login
            )
            .formLogin(); // default login form for protected endpoints

        return http.build();
    }
}

