package com.prestobr.fiscal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

// Classe de configuração responsável por definir as regras de segurança HTTP da aplicação

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // desativa proteção CSRF (desnecessária para API REST)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // libera todos os endpoints

        return http.build();
    }
}
