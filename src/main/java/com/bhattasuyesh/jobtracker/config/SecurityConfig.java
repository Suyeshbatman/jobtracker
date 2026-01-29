package com.bhattasuyesh.jobtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                // REST API style: no sessions, no login forms
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public API endpoints
                        .requestMatchers("/health", "/health/**", "/auth/**").permitAll()

                        // React build/static files
                        .requestMatchers(
                                "/", "/index.html",
                                "/assets/**",
                                "/*.ico", "/*.png", "/*.svg", "/*.css", "/*.js"
                        ).permitAll()

                        // React Router routes (hard refresh / direct open)
                        .requestMatchers("/login", "/register", "/dashboard", "/dashboard/**").permitAll()

                        // ✅ Secure only backend data endpoints
                        .requestMatchers("/me", "/applications/**").authenticated()

                        // ✅ Everything else is allowed (so SPA forwarding doesn’t get blocked)
                        .anyRequest().permitAll()
                )

                // Add JWT filter before Spring’s default auth filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
