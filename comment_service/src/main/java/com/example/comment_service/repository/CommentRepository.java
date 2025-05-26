
package com.example.comment_service.repository;

import com.example.comment_service.model.Comment;
import com.example.comment_service.model.Post;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    List<Comment> findByUserId(Long userId);
}
