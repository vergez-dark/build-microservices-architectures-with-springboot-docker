package com.jeffrey.POST_SERVICE.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.jeffrey.POST_SERVICE.models.Comment;

@FeignClient(name = "COMMENT-SERVICE")
public interface CommentServiceClient {
    @GetMapping("/comments/post/{postId}")
    ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable("postId") Long postId);
}
