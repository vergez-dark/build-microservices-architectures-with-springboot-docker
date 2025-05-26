
package com.example.comment_service.service;

import com.example.comment_service.dto.CommentDto;
import com.example.comment_service.dto.CommentRequest;
import com.example.comment_service.dto.UsersDto;
import com.example.comment_service.model.Comment;
import com.example.comment_service.model.Post;
import com.example.comment_service.model.Users;
import com.example.comment_service.repository.CommentRepository;
import com.example.comment_service.repository.PostServiceClient;
import com.example.comment_service.repository.UserServiceClient;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final CommentRepository repository;
    private final PostServiceClient postServiceClient;
    private final UserServiceClient userServiceClient;

    public List<Comment> getAll() {
        return repository.findAll();
    }

    public Comment findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public Comment create(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        Post post = postServiceClient.getPostById(commentRequest.getPostId());
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        comment.setPost(post);
        Users user = userServiceClient.findUsersById(commentRequest.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        comment.setUser(new UsersDto(user));
        comment.setPostId(commentRequest.getPostId());
        comment.setUserId(commentRequest.getUserId());
        comment.setCreatedOn(new Date());
        return repository.save(comment);
    }

    public List<Comment> findByPostId(Long post_id) {
        List<Comment> comments = repository.findByPostId(post_id);
        comments.stream().forEach(
                comment -> comment.setUser(new UsersDto(userServiceClient.findUsersById(comment.getUserId()))));
        return repository.findByPostId(post_id);
    }

    public List<Comment> findByUserId(Long user_id) {
        List<Comment> comments = repository.findByUserId(user_id);
        comments.stream().forEach(comment -> comment.setPost(postServiceClient.getPostById(comment.getPostId())));
        return comments;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Comment save(Comment comment) {
        return repository.save(comment);
    }
}
