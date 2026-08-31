package com.qmcms.config;

import com.qmcms.security.filter.JwtAuthenticationFilter;
import com.qmcms.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtFilter;

    private final CustomUserDetailsService customUserDetailsService;


    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    // =========================================================
    // AUTHENTICATION MANAGER
    // =========================================================

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();

    }


    // =========================================================
    // AUTHENTICATION PROVIDER
    // =========================================================

    @Bean
    AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                customUserDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;

    }


    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:4200"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept",
                        "Origin"
                )
        );

        configuration.setExposedHeaders(
                List.of(
                        "Authorization"
                )
        );

        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;

    }


    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // ================================
                // CORS
                // ================================

                .cors(
                        Customizer.withDefaults()
                )


                // ================================
                // CSRF
                // ================================

                .csrf(
                        csrf -> csrf.disable()
                )


                // ================================
                // SESSION
                // ================================

                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )


                // ================================
                // AUTHORIZATION
                // ================================

                .authorizeHttpRequests(
                        auth -> auth

                                // ==================
                                // AUTH
                                // ==================

                                .requestMatchers(
                                        "/api/auth/**"
                                )
                                .permitAll()


                                // ==================
                                // PROFILE
                                // ==================

                                .requestMatchers(
                                        "/api/profile/**"
                                )
                                .authenticated()


                                // ==================
                                // EVERYTHING ELSE
                                // ==================

                                .anyRequest()
                                .authenticated()
                )


                // ================================
                // AUTHENTICATION PROVIDER
                // ================================

                .authenticationProvider(
                        authenticationProvider()
                )


                // ================================
                // JWT FILTER
                // ================================

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();

    }

}