package com.jeffrey.POST_SERVICE.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostResponse extends BaseModelDTO {
    private Long id;
    private String title;
    private String content;
    private Long user_id;
    private UserResponse user;
    private String slug;
    private CategoryResponse category;
    private List<CommentResponse> comments = new ArrayList<>();
    private List<MediaResponse> media =  new ArrayList<>();
 
    
}
