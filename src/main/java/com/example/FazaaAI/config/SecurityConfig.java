package com.example.FazaaAI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUserFilter jwtUserFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF since we're using JWT
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Public endpoints
                        .anyRequest().authenticated() // All other endpoints require a token
                )
                .addFilterBefore(jwtUserFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }
}