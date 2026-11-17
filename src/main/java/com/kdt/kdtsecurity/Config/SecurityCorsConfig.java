package com.kdt.kdtsecurity.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

// Spring이 이 클래스의 설정과 @Bean 메서드를 읽어 적용합니다.
@Configuration
public class SecurityCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api.**")
                .allowedOrigins("http://127.0.0.1:8000")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD")
                .allowedHeaders("Content-Type", "Authorization", "X-Api-Key");
    }
}