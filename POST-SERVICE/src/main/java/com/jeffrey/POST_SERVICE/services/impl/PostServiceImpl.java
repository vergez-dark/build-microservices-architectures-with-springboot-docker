package com.jeffrey.POST_SERVICE.services.impl;

import java.util.List;

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

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        ResponseEntity<User> user  = userServiceClient.findUserById(postRequest.getUser_id());
       if (!user.getStatusCode().equals(HttpStatus.OK)) {
            throw new ResourceNotFoundException("User", "id", postRequest.getUser_id());
        }
        if (postRequest.getCategory_id() == null) {
            throw new ResourceNotFoundException("Category", "id", postRequest.getCategory_id());
        
       }
        Category category = categoryRepository.findById(postRequest.getCategory_id())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postRequest.getCategory_id()));
        
        Post post = postMapper.toPost(postRequest);
        post.setCategory(category);
        post.setUser(user.getBody());
        post.setSlug(postRequest.getTitle().toLowerCase().replaceAll(" ", "-"));
        Post savedPost = postRepository.save(post);
        PostResponse savedPostResponse = postMapper.toPostResponse(savedPost);
        UserResponse userMapped = userMapper.toUserResponse(savedPost.getUser());
        savedPostResponse.setUser(userMapped);
        return savedPostResponse;
        
    }

    @Override
    public PostResponses getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, 
            sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponse> postResponses = posts.stream()
            .map(post -> {
                ResponseEntity <List<Comment>> comments = commentServiceClient.getCommentsByPostId(post.getId());
                ResponseEntity <List <Media>> media = mediaServiceClient.getMediaByPostId(post.getId());
                PostResponse postResponse = postMapper.toPostResponse(post);
                postResponse.setMedia(mediaMapper.toMediaResponse(media.getBody()));
                ResponseEntity<User> user  = userServiceClient.findUserById(post.getUser_id());
                postResponse.setUser(userMapper.toUserResponse(user.getBody()));
                postResponse.setComments(commentMapper.toCommentResponse(comments.getBody()));
                return postResponse;
            }).toList();
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
        PostResponse postResponse = postMapper.toPostResponse(post);
        postResponse.setCategory(categoryMapper.toCategoryResponse(post.getCategory()));
        ResponseEntity<User> user  = userServiceClient.findUserById(post.getUser_id());
        postResponse.setUser(userMapper.toUserResponse(user.getBody()));
        ResponseEntity <List<Comment>> comments = commentServiceClient.getCommentsByPostId(post.getId());
        ResponseEntity <List <Media>> media = mediaServiceClient.getMediaByPostId(post.getId());
        postResponse.setMedia(mediaMapper.toMediaResponse(media.getBody()));
        postResponse.setComments(commentMapper.toCommentResponse(comments.getBody()));
        return postResponse;
    }

    @Override
    public PostResponse updatePost(PostRequest postRequest, long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        ResponseEntity<User> user = userServiceClient.findUserById(postRequest.getUser_id());
        if (!user.getStatusCode().equals(HttpStatus.OK)) {
            throw new ResourceNotFoundException("User", "id", postRequest.getUser_id());
        }
        Category category = categoryRepository.findById(postRequest.getCategory_id())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", postRequest.getCategory_id()));

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setCategory(category);
        post.setUser(user.getBody());
        post.setSlug(postRequest.getTitle().toLowerCase().replaceAll(" ", "-"));
        post.setUser_id(postRequest.getUser_id());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);
        PostResponse updatedPostResponse = postMapper.toPostResponse(updatedPost);
        ResponseEntity <List<Comment>> comments = commentServiceClient.getCommentsByPostId(updatedPost.getId());
        ResponseEntity <List <Media>> media = mediaServiceClient.getMediaByPostId(updatedPost.getId());
        updatedPostResponse.setMedia(mediaMapper.toMediaResponse(media.getBody()));
        updatedPostResponse.setComments(commentMapper.toCommentResponse(comments.getBody()));
        updatedPostResponse.setUser(userMapper.toUserResponse(updatedPost.getUser()));
        return updatedPostResponse;
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    @Override
    public List<PostResponse> getPostsByCategory(Long category_id) {
        categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", category_id));
        List<Post> posts = postRepository.findByCategoryId(category_id);
        List<PostResponse> postResponses = posts.stream()
            .map(post -> {
                ResponseEntity <List<Comment>> comments = commentServiceClient.getCommentsByPostId(post.getId());
                ResponseEntity <List <Media>> media = mediaServiceClient.getMediaByPostId(post.getId());
                PostResponse postResponse = postMapper.toPostResponse(post);
                ResponseEntity<User> user  = userServiceClient.findUserById(post.getUser_id());
                if (!user.getStatusCode().equals(HttpStatus.OK)) {
                    throw new ResourceNotFoundException("User", "id", post.getUser_id());
                }
                postResponse.setUser(userMapper.toUserResponse(user.getBody()));
                postResponse.setComments(commentMapper.toCommentResponse(comments.getBody()));
                postResponse.setMedia(mediaMapper.toMediaResponse(media.getBody()));
                return postResponse;
            }).toList();
        return postResponses;
    }
    @Override
    public PostResponse getPostsBySlug(String slug)  {
        Post post = postRepository.findBySlug(slug);
        PostResponse postResponse = postMapper.toPostResponse(post);
        ResponseEntity<User> user  = userServiceClient.findUserById(post.getUser_id());
        postResponse.setUser(userMapper.toUserResponse(user.getBody()));
        ResponseEntity <List<Comment>> comments = commentServiceClient.getCommentsByPostId(post.getId());
        ResponseEntity <List <Media>> media = mediaServiceClient.getMediaByPostId(post.getId());
        postResponse.setMedia(mediaMapper.toMediaResponse(media.getBody()));
        postResponse.setComments(commentMapper.toCommentResponse(comments.getBody()));
        return postResponse;
    }
    

    
}
