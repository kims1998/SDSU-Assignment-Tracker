package com.sdsu.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Allow react to talk to springboot for all /api/* endpoints
                .allowedOrigins("http://localhost:3000") // React/Frontend Address == CHANGE THIS WHEN WE FIGURE OUT AWS
                                                         // ==
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // Allow all
                .allowCredentials(true);
    }
}
