package com.blog.authentications.model;

import com.blog.authentications.entities.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor @Builder @Data
public class Comment extends EntityBase {

    private String content;
    private Post post;
    private Users user;
}
