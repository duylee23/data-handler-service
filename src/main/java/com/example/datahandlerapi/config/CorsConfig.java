package com.example.datahandlerapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOriginPatterns("*") // ✅ SỬ DỤNG CÁI NÀY
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}