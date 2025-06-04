package com.jeffrey.POST_SERVICE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jeffrey.POST_SERVICE.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByCategoryId(Long category_id);
    Post findBySlug(String slug);

    @Query("SELECT p FROM Post p WHERE p.user_id = :user_id")
    List<Post> findByUserId(Long user_id);
    
}
