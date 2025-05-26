package com.jeffrey.POST_SERVICE.dto;

import com.jeffrey.POST_SERVICE.models.Post;
import com.jeffrey.POST_SERVICE.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CommentResponse extends BaseModelDTO {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private User user;
    private Post post;

}
