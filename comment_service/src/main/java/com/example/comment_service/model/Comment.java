package com.example.comment_service.model;

import com.example.comment_service.model.Post;
import com.example.comment_service.model.Users;
import com.example.comment_service.dto.UsersDto;
import com.example.comment_service.model.EntityBase;
import com.example.comment_service.model.EntityBaseDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Transient;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Comment extends EntityBase {

    private String content;
    private Long postId;
    private Long userId;

    @Transient
    private Post post;

    @Transient
    private UsersDto user;
}
