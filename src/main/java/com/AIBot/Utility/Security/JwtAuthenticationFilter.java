package com.AIBot.Utility.Security;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.AIBot.Utility.CommonUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtUtil.validateToken(token);
                // You can log claims if needed:
                CommonUtils.logMethodEntry(this, "Token valid for: " + claims.getSubject());
            } catch (ExpiredJwtException ex) {
                sendError(response, "Token expired", 498);
                return;
            } catch (JwtException | IllegalArgumentException ex) {
                sendError(response, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            sendError(response, "Missing token", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" + message + "\", \"status\": " + status + "}");
    }
}