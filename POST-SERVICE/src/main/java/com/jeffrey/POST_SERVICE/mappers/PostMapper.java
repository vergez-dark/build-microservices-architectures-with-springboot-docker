package com.jeffrey.POST_SERVICE.mappers;

import org.mapstruct.Mapper;

import com.jeffrey.POST_SERVICE.dto.PostRequest;
import com.jeffrey.POST_SERVICE.dto.PostResponse;
import com.jeffrey.POST_SERVICE.models.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
        
    PostResponse toPostResponse(Post post);
    Post toPost (PostRequest postRequest);
}
