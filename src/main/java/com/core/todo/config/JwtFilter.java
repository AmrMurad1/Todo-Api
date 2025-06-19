package com.core.todo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
         @NonNull HttpServletRequest request,
         @NonNull HttpServletResponse response,
         @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String AuthHeader = request.getHeader("Authorization");
        final String token;
        if (AuthHeader == null && !AuthHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }
        token = AuthHeader.substring(7);

    }
}
