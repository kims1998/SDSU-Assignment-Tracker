// ==================================================
// DELETE THIS FILE AFTER LEARNING SECURITY/AUTH STUFF
// THIS FILE BYPASSES BACKEND LOGIN REQUIREMENT
// ==================================================
package com.sdsu.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TemporaryOffSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // Allow all endpoints
        return http.build();
    }
}