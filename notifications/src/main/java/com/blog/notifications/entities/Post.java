package com.blog.notifications.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
