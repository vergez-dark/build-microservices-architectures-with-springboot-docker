package com.example.comment_service.dto;

import com.example.comment_service.model.Comment;
import com.example.comment_service.model.EntityBaseDTO;
import com.example.comment_service.model.Post;
import com.example.comment_service.model.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentDto extends EntityBaseDTO {
    private String content;
    private Post post;
    private Users user;

    public CommentDto(Comment comment) {
        this.content = comment.getContent();
        this.createdOn = comment.getCreatedOn();
    }
}
