package com.jeffrey.POST_SERVICE.repository;

import com.jeffrey.POST_SERVICE.models.Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.jeffrey.POST_SERVICE.models.User;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    
    @GetMapping("/api/v1/users/{id}")
    ResponseEntity <User> findUserById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/users/{id}")
    User findUsersById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/users/token")
    Token findByTk(@RequestParam String tk);

    @GetMapping("/api/v1/users/name")
    User findByUsername(@RequestParam String username);
}
