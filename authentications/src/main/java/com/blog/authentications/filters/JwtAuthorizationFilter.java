package com.blog.authentications.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.blog.authentications.entities.Token;
import com.blog.authentications.repo.TokenRespository;
import com.blog.authentications.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenRespository tokenRespository;

    public JwtAuthorizationFilter(TokenRespository tokenRespository) {
        this.tokenRespository = tokenRespository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/refreshToken")) {
            filterChain.doFilter(request, response);
        }else {
            String authorizationHeader = request.getHeader(JWTUtil.AUTH_HEADER);

            if (authorizationHeader != null && authorizationHeader.startsWith(JWTUtil.TOKEN_PREFIX)) {
                handleAuthorization(request, response, filterChain, authorizationHeader);
            }else {
                filterChain.doFilter(request, response);
            }
        }
    }

    private void handleAuthorization(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String authorizationHeader) throws IOException {
        try {
            String jwt = authorizationHeader.substring(7);
            Token token = tokenRespository.findByTk(jwt).orElse(null);
            if (token.isRevoked() || token != null){
                throw new RuntimeException("vous devez vous authentifiez !");
            }
            Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setHeader("error-message", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
