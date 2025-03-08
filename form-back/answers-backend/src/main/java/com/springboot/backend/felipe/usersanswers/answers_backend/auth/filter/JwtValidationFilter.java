package com.springboot.backend.felipe.usersanswers.answers_backend.auth.filter;

import static com.springboot.backend.felipe.usersanswers.answers_backend.auth.TokenJwtConfig.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.backend.felipe.usersanswers.answers_backend.auth.SimpleGrantedAuthorityJsonCreator;
import com.springboot.backend.felipe.usersanswers.answers_backend.entities.User;
import com.springboot.backend.felipe.usersanswers.answers_backend.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidationFilter extends BasicAuthenticationFilter {

    @Autowired
    private UserService userService;

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        // Si no hay header o no empieza con "Bearer", manejar como token inválido
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            handleInvalidToken(null, response); // No hay ID disponible
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");
        try {
            // Validar el token
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            Long userId = claims.get("userId", Long.class); // Obtener el ID del usuario del token
            Object authoritiesClaims = claims.get("authorities");

            // Convertir roles a objetos Spring Security
            Collection<? extends GrantedAuthority> roles = Arrays.asList(
                    new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                            .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
            );

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, roles);
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);

        } catch (JwtException e) {
            // Extraer el ID del usuario del token si está disponible
            Long userId = extractUserIdFromToken(token);
            handleInvalidToken(userId, response);
        }
    }

    private void handleInvalidToken(Long userId, HttpServletResponse response) throws IOException {
        if (userId != null) {
            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Incrementar intentos fallidos
                userService.increaseFailedAttempts(user.getId());

                // Verificar si la cuenta está bloqueada
                if (userService.isAccountLocked(user.getId())) {
                    Map<String, String> body = new HashMap<>();
                    body.put("message", "Tu cuenta ha sido bloqueada debido a múltiples intentos fallidos.");
                    body.put("error", "Cuenta bloqueada");

                    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
                    return;
                }
            }
        }

        // Respuesta estándar para token inválido
        Map<String, String> body = new HashMap<>();
        body.put("message", "Token inválido o ausente.");
        body.put("error", "Acceso no autorizado");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
    }

    private Long extractUserIdFromToken(String token) {
        try {
            // Parsear el token para extraer el ID del usuario
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("userId", Long.class);
        } catch (Exception e) {
            return null; // No se pudo extraer el ID
        }
    }
}