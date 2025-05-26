package com.example.comment_service.repository;

import com.example.comment_service.model.Post;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "POST-SERVICE")
public interface PostServiceClient {
    @GetMapping("/api/posts/{id}")
    Post getPostById(@PathVariable("id") Long id);
}