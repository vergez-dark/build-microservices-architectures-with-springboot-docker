package com.blog.authentications.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blog.authentications.entities.Token;
import com.blog.authentications.entities.Users;
import com.blog.authentications.exceptions.NoActiveUserException;
import com.blog.authentications.model.ApiError;
import com.blog.authentications.repo.AppUserRepository;
import com.blog.authentications.repo.TokenRespository;
import com.blog.authentications.request.LoginRequest;
import com.blog.authentications.response.LoginResponse;
import com.blog.authentications.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //cet Objet va se charger de en valider les informations fournies et établir une session authentifiée pour l'utilisateur.
    private final AuthenticationManager authenticationManager;

    private final TokenRespository tokenRespository;

    private final AppUserRepository appUserRepository;

    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,TokenRespository tokenRespository, AppUserRepository appUserRepository, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenRespository = tokenRespository;
        this.appUserRepository = appUserRepository;
        this.objectMapper = objectMapper;
    }


    //la methode qui va s'executer quand l'utilisateur va tenter de s'authentifier
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String contentType = request.getContentType();

            String login;
            String password;

            if (contentType != null && contentType.contains("application/json")) {
                LoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
                login = credentials.getLogin();
                password = credentials.getPassword();
            } else {
                throw new RuntimeException("Unsupported Content-Type: " + contentType);
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(login, password);

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("Error reading authentication request", e);
        }
    }



    //la methode qui va s'executer si l'authentification reussi
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try{
            System.out.println("successfulAuthentication");
            User user = (User) authResult.getPrincipal();//retourne l'utilisateur authentifier
            Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);

            String jwtAccessToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_ACCESS_TOKEN))
                    .withIssuer(request.getRequestURL().toString())//nom de l'aplication qui a generer le token;
                    .withClaim("roles", user.getAuthorities().stream().map(ga->ga.getAuthority()).collect(Collectors.toList()))
                    .sign(algorithm);

            String jwtRefreshToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_REFRESH_TOKEN))
                    .withIssuer(request.getRequestURL().toString())//nom de l'aplication qui a generer le token;
                    .sign(algorithm);

            Users appUser = appUserRepository.findByUsername(user.getUsername()).orElseThrow(()->{
                throw new RuntimeException("user not found");
            });

                if (!appUser.isActive()){
                    throw new NoActiveUserException("Inactive User Account");
                }

            Token token = tokenRespository.save(new Token(null, jwtAccessToken,false,false));
            System.out.println(appUser);


            LoginResponse loginResponse = new LoginResponse(jwtAccessToken, jwtRefreshToken, appUser);
    //        response.setHeader("Authorization", jwtAccessToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getOutputStream(), loginResponse);
        }catch (NoActiveUserException e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ApiError apiError = new ApiError();
            apiError.setMessage(e.getMessage());
            apiError.setTimestamp(LocalDateTime.now());
            apiError.setCode(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(response.getOutputStream(), apiError);

        }
    }
}
