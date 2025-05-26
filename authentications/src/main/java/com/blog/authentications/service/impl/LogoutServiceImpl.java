package com.blog.authentications.service.impl;

import com.blog.authentications.entities.Token;
import com.blog.authentications.repo.TokenRespository;
import com.blog.authentications.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutServiceImpl implements LogoutService {

    private final TokenRespository tokenRespository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        jwt = authHeader.substring(7);

        Token token = tokenRespository.findByTk(jwt).orElse(null);

        if (token != null) {
            token.setRevoked(true);
            token.setExpired(true);
            tokenRespository.save(token);
        }
    }
}
