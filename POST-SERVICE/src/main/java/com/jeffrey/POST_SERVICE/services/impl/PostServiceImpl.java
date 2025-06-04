package com.jeffrey.POST_SERVICE.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeffrey.POST_SERVICE.dto.PostRequest;
import com.jeffrey.POST_SERVICE.dto.PostResponse;
import com.jeffrey.POST_SERVICE.dto.PostResponses;
import com.jeffrey.POST_SERVICE.dto.UserResponse;
import com.jeffrey.POST_SERVICE.exceptions.ResourceNotFoundException;
import com.jeffrey.POST_SERVICE.mappers.CategoryMapper;
import com.jeffrey.POST_SERVICE.mappers.CommentMapper;
import com.jeffrey.POST_SERVICE.mappers.MediaMapper;
import com.jeffrey.POST_SERVICE.mappers.PostMapper;
import com.jeffrey.POST_SERVICE.mappers.UserMapper;
import com.jeffrey.POST_SERVICE.models.Category;
import com.jeffrey.POST_SERVICE.models.Post;
import com.jeffrey.POST_SERVICE.models.User;
import com.jeffrey.POST_SERVICE.repository.CategoryRepository;
import com.jeffrey.POST_SERVICE.repository.CommentServiceClient;
import com.jeffrey.POST_SERVICE.repository.MediaServiceClient;
import com.jeffrey.POST_SERVICE.repository.PostRepository;
import com.jeffrey.POST_SERVICE.repository.UserServiceClient;
import com.jeffrey.POST_SERVICE.services.PostService;
import com.jeffrey.POST_SERVICE.models.Comment;
import com.jeffrey.POST_SERVICE.models.Media;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserServiceClient userServiceClient;
    private final CommentServiceClient commentServiceClient;
    private final CategoryRepository categoryRepository;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final MediaServiceClient mediaServiceClient;
    private final MediaMapper mediaMapper;
    private final CategoryMapper categoryMapper;

    private User fetchUserWithFallback(Long userId) {
        try {
            ResponseEntity<User> response = userServiceClient.findUserById(userId);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return null;
    }

    private List<Comment> fetchCommentsWithFallback(Long postId) {
        try {
            ResponseEntity<List<Comment>> response = commentServiceClient.getCommentsByPostId(postId);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return List.of();
    }

    private List<Media> fetchMediaWithFallback(Long postId) {
        try {
            ResponseEntity<List<Media>> response = mediaServiceClient.getMediaByPostId(postId);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            // Log error if needed
        }
        return List.of();
    }

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        User user = fetchUserWithFallback(postRequest.getUser_id());
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", postRequest.getUser_id());
        }

        Category category = categoryRepository.findById(postRequest.getCategory_id())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postRequest.getCategory_id()));
        
        Post post = postMapper.toPost(postRequest);
        post.setCategory(category);
        post.setUser(user);
        post.setSlug(generateSlug(postRequest.getTitle()));
        
        Post savedPost = postRepository.save(post);
        return buildPostResponse(savedPost);
    }

    @Override
    public PostResponses getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, 
            sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponse> postResponses = posts.stream()
            .map(this::buildPostResponse)
            .toList();

        PostResponses postResponsesPage = new PostResponses();
        postResponsesPage.setContent(postResponses);
        postResponsesPage.setPageNo(posts.getNumber());
        postResponsesPage.setPageSize(posts.getSize());
        postResponsesPage.setTotalElements(posts.getTotalElements());
        postResponsesPage.setTotalPages(posts.getTotalPages());
        postResponsesPage.setLast(posts.isLast());
        return postResponsesPage;
    }

    @Override
    public PostResponse getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return buildPostResponse(post);
    }

    @Override
    public PostResponse updatePost(PostRequest postRequest, long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        User user = fetchUserWithFallback(postRequest.getUser_id());
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", postRequest.getUser_id());
        }

        Category category = categoryRepository.findById(postRequest.getCategory_id())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postRequest.getCategory_id()));

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setCategory(category);
        post.setUser(user);
        post.setSlug(generateSlug(postRequest.getTitle()));
        post.setUser_id(postRequest.getUser_id());

        Post updatedPost = postRepository.save(post);
        return buildPostResponse(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post", "id", id);
        }
        postRepository.deleteById(id);
    }

    @Override
    public List<PostResponse> getPostsByCategory(Long category_id) {
        if (!categoryRepository.existsById(category_id)) {
            throw new ResourceNotFoundException("Category", "id", category_id);
        }
        
        return postRepository.findByCategoryId(category_id).stream()
            .map(this::buildPostResponse)
            .toList();
    }

    @Override
    public PostResponse getPostsBySlug(String slug) {
        Post post = Optional.ofNullable(postRepository.findBySlug(slug))
                .orElseThrow(() -> new ResourceNotFoundException("Post" , "slug", 1));
        return buildPostResponse(post);
    }

    private PostResponse buildPostResponse(Post post) {
        PostResponse response = postMapper.toPostResponse(post);
        response.setCategory(categoryMapper.toCategoryResponse(post.getCategory()));
        
        User user = post.getUser() != null ? post.getUser() : fetchUserWithFallback(post.getUser_id());
        if (user != null) {
            response.setUser(userMapper.toUserResponse(user));
        }
        
        List<Comment> comments = fetchCommentsWithFallback(post.getId());
        response.setComments(commentMapper.toCommentResponse(comments));
        
        List<Media> media = fetchMediaWithFallback(post.getId());
        response.setMedia(mediaMapper.toMediaResponse(media));
        
        return response;
    }

    private String generateSlug(String title) {
        return title.toLowerCase().replaceAll(" ", "-");
    }

    @Override
    public List<PostResponse> getPostsByUserId(Long user_id) {
        // if (!categoryRepository.existsById(user_id)) {
        //     throw new ResourceNotFoundException("User", "id", user_id);
        // }
        
        return postRepository.findByUserId(user_id).stream()
            .map(this::buildPostResponse)
            .toList();
    }
}