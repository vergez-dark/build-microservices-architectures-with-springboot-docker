package com.jeffrey.POST_SERVICE.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseModel {

    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private User user;
    private Post post;

}
