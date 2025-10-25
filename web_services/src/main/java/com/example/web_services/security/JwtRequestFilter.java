package com.example.web_services.security; 

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtilService jwtUtilService;

    public JwtRequestFilter(JwtUtilService jwtUtilService) {
        this.jwtUtilService = jwtUtilService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String testUserIdHeader = request.getHeader("X-Test-User-ID");
        String jwt = null;
        Long userId = null;
        String role = "USER";

        // Extraer el header y el token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                // Validar el token y extraer los datos
                if (jwtUtilService.isTokenValid(jwt)) {
                    userId = jwtUtilService.extractId(jwt);
                    role = jwtUtilService.extractRole(jwt);
                }
            } catch (Exception e) {
                logger.warn("Error al parsear el token JWT: " + e.getMessage());
            }
        } else if (testUserIdHeader != null && !testUserIdHeader.isEmpty()) {
            try {
                logger.warn("MODO DE PRUEBA SWAGGER: Autenticando como usuario ID " + testUserIdHeader);
                userId = Long.parseLong(testUserIdHeader);
                // (Usamos el 'role' de relleno "USER")
            } catch (NumberFormatException e) {
                logger.warn("Header X-Test-User-ID inválido (no es un número): " + testUserIdHeader);
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
            
            UserDetails userDetails = new User(userId.toString(), "", authorities);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}