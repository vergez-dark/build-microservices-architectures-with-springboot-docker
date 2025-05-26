package com.jeffrey.POST_SERVICE.dto;

import java.util.ArrayList;
import java.util.List;

import com.jeffrey.POST_SERVICE.models.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse  extends BaseModelDTO {
    
    private Long id;
    private String name;
    private String slug;
    private List<Post> posts = new ArrayList<>();
}
