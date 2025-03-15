package com.example.FazaaAI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private JwtUserFilter jwtUserFilter;

    @Bean
    public FilterRegistrationBean<JwtUserFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtUserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtUserFilter);
        registrationBean.addUrlPatterns("/api/*"); // Apply to all /api endpoints
        return registrationBean;
    }
}