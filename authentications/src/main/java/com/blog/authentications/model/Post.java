package com.blog.authentications.model;

import com.blog.authentications.entities.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder @AllArgsConstructor @NoArgsConstructor @Data
public class Post extends EntityBase {

    private String title;
    private String content;
    private String slug;
    private String statusPost;
    private Users user;
    private List<Comment> comments;
    private Category category;
}
