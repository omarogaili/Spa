package com.Spa.spa.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Spa.spa.Components.TokenFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private TokenFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/api/customers/booking-interface" , 
            "/api/customers/book-appointment", "/api/customers/cancel-appointment/{id}").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

}
