package com.blog.authentications.config;


import com.blog.authentications.filters.JwtAuthenticationFilter;
import com.blog.authentications.filters.JwtAuthorizationFilter;
import com.blog.authentications.repo.AppUserRepository;
import com.blog.authentications.repo.TokenRespository;
import com.blog.authentications.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final TokenRespository tokenRepository;

    private final AppUserRepository appUserRepository;

    private final ObjectMapper objectMapper;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration, TokenRespository tokenRepository, AppUserRepository appUserRepository, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.tokenRepository = tokenRepository;
        this.appUserRepository = appUserRepository;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                ).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v2/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/api/v1/test",
                                "/api/v1/signup",
                                "/api/v1/activate"
                        ).permitAll()
                        .requestMatchers("/h2-console/**", "/api/v1/refreshToken/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/users/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                        .logout(logout->logout.disable())
                        .addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenRepository, appUserRepository, objectMapper))
                        .addFilterBefore(new JwtAuthorizationFilter(tokenRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

























    /* Statefull authentication
        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        .csrf(AbstractHttpConfigurer::disable)
        http.headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/resources/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser user = accountService.loadUserByUsername(username);
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                user.getAppRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
                return new User(user.getUsername(),user.getPassword(),authorities);
            }
        });
    }
    */
}
