package com.jeffrey.POST_SERVICE.controllers;


import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeffrey.POST_SERVICE.dto.PostRequest;
import com.jeffrey.POST_SERVICE.dto.PostResponse;
import com.jeffrey.POST_SERVICE.dto.PostResponses;
import com.jeffrey.POST_SERVICE.services.PostService;
import com.jeffrey.POST_SERVICE.utils.AppConstants;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    // get all posts

    @GetMapping
    public ResponseEntity<PostResponses> getAllPosts( @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        PostResponses posts = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(posts);
    }

    // get posts by id
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("id") long id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    // get posts by category id
    @GetMapping("/category/{category_id}")
    public ResponseEntity<List<PostResponse>> getPostsByCategory(@PathVariable("category_id") Long category_id) {
        List <PostResponse> posts = postService.getPostsByCategory(category_id);
        return ResponseEntity.ok(posts);
    }
    // get posts by slug
    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostResponse> getPostsBySlug(@PathVariable("slug") String slug) {
        PostResponse post = postService.getPostsBySlug(slug);
        return ResponseEntity.ok(post);
    }

    // create post
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        PostResponse post = postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(post);
    }

    // update post
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable("id") long id, @RequestBody PostRequest postRequest) {
        PostResponse post = postService.updatePost(postRequest, id);
        return ResponseEntity.ok(post);
    }

    // delete post
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
        postService.deletePostById(id);
        return ResponseEntity.ok("Post deleted successfully!");
    }
    
    
}
