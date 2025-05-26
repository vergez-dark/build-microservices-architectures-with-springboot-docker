package com.jeffrey.POST_SERVICE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeffrey.POST_SERVICE.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByCategoryId(Long category_id);
    Post findBySlug(String slug);
    
}
