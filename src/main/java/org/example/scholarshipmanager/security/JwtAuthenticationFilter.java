package org.example.scholarshipmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Filter responsible for JWT authentication.
 * <p>
 * This filter extracts a JWT token from the HTTP request, validates it,
 * and if valid, sets the authentication information in the Spring Security context.
 * It supports extracting the JWT either from a cookie named "jwt_token" or
 * from the "Authorization" header with the Bearer scheme.
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Filters each HTTP request to check for a valid JWT token.
     * <p>
     * If a valid JWT is found, the user's authentication is set in the SecurityContext,
     * enabling Spring Security to authorize subsequent requests.
     * </p>
     *
     * @param request     the incoming HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain to pass control to the next filter
     * @throws ServletException if an exception occurs during request processing
     * @throws IOException      if an I/O error occurs during request processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (jwt != null && jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                String role = jwtUtil.getRoleFromToken(jwt);
                Integer userId = jwtUtil.getUserIdFromToken(jwt);

                if (username != null && role != null && userId != null) {
                    request.setAttribute("userId", userId);
                    request.setAttribute("userRole", role);
                    request.setAttribute("username", username);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authentication set for user: {} with role: {}", username, role);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the HTTP request.
     * <p>
     * This method first tries to get the JWT from a cookie named "jwt_token".
     * If the cookie is not present or does not contain a valid token,
     * it falls back to checking the "Authorization" header for a Bearer token.
     * </p>
     *
     * @param request the HTTP request
     * @return the JWT token string if present, otherwise null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            Cookie jwtCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt_token".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);

            if (jwtCookie != null && jwtCookie.getValue() != null) {
                logger.debug("JWT found in cookie");
                return jwtCookie.getValue();
            }
        }

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            logger.debug("JWT found in Authorization header");
            return bearerToken.substring(7);
        }

        return null;
    }
}
