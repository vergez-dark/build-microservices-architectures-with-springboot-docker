package com.blog.notifications.client;

import com.blog.notifications.entities.Users;
import com.blog.notifications.models.Token;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{id}")
    Users findUsersById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/users/token")
    Token findByTk(@RequestParam String tk);

    @GetMapping("/api/v1/users/name")
    Users findByUsername(@RequestParam String username);
}
