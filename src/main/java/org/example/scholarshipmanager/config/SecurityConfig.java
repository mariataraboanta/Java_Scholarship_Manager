package org.example.scholarshipmanager.config;

import org.example.scholarshipmanager.security.JwtAuthenticationFilter;
import org.example.scholarshipmanager.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for the Scholarship Manager application.
 *
 * This class configures Spring Security settings including JWT authentication,
 * authorization rules, password encoding, and security filters. It implements
 * a stateless authentication mechanism using JWT tokens and provides role-based
 * access control for different user types (ADMIN, STUDENT).
 *
 * The configuration disables CSRF protection since JWT tokens are used for
 * authentication and enables method-level security annotations.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Custom user details service for loading user-specific data during authentication.
     * This service is responsible for retrieving user information from the database.
     */
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * JWT authentication filter that processes JWT tokens from incoming requests.
     * This filter validates JWT tokens and sets up the security context for authenticated users.
     */
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Creates a password encoder bean using BCrypt hashing algorithm.
     *
     * BCrypt is a strong hashing function designed for password storage,
     * providing salt generation and configurable work factor for security.
     *
     * @return BCryptPasswordEncoder instance for encoding and validating passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a DAO authentication provider that uses the custom user details service
     * and password encoder for user authentication.
     *
     * This provider handles the authentication process by loading user details
     * and comparing encoded passwords during login attempts.
     *
     * @return configured DaoAuthenticationProvider instance
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Creates an authentication manager bean from the provided authentication configuration.
     *
     * The authentication manager is responsible for processing authentication requests
     * and coordinating with authentication providers.
     *
     * @param config the authentication configuration
     * @return AuthenticationManager instance
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain with comprehensive security settings.
     *
     * This method sets up:
     * - CSRF protection (disabled for stateless JWT authentication)
     * - Session management (stateless for JWT)
     * - URL-based authorization rules with role-based access control
     * - Authentication provider configuration
     * - JWT filter integration
     * - Form login configuration (disabled in favor of JWT)
     * - Logout handling with JWT token cleanup
     *
     * Access Control Rules:
     * - Public access: Static resources, API endpoints, auth pages
     * - ADMIN role: All /admin/** endpoints
     * - STUDENT role: /scholarship-matches, /my-applications endpoints
     * - Authenticated: All other endpoints require authentication
     *
     * @param http the HttpSecurity configuration object
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/favicon.ico",
                                "/home.html",
                                "/index.html",
                                "/api/**",
                                "/",
                                "/about",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**",
                                "/auth/login.html",
                                "/auth/register.html"
                        ).permitAll()

                        // Admin endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Student endpoints
                        .requestMatchers("/scholarship-matches", "/my-applications").hasRole("STUDENT")

                        // Any other request needs authentication
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .permitAll()
                        .disable()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .deleteCookies("jwt_token")
                        .permitAll()
                );

        return http.build();
    }
}