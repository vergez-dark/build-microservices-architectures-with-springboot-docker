package com.jeffrey.POST_SERVICE.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.jeffrey.POST_SERVICE.dto.CommentResponse;
import com.jeffrey.POST_SERVICE.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    List<CommentResponse> toCommentResponse(List<Comment> commentList);
    
}
