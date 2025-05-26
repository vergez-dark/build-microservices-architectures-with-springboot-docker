package com.jeffrey.POST_SERVICE.services;

import java.util.List;

import com.jeffrey.POST_SERVICE.dto.PostRequest;
import com.jeffrey.POST_SERVICE.dto.PostResponse;
import com.jeffrey.POST_SERVICE.dto.PostResponses;

public interface PostService {
    
    PostResponse createPost(PostRequest postRequest);

    PostResponses getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostResponse getPostById(long id);

    PostResponse updatePost(PostRequest postRequest, long id);

    void deletePostById(long id);

    List<PostResponse> getPostsByCategory(Long category_id);
    PostResponse getPostsBySlug(String slug);
}
