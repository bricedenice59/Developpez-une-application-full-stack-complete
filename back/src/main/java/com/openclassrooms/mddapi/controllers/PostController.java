package com.openclassrooms.mddapi.controllers;


import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.payloads.requests.CommentPostRequest;
import com.openclassrooms.mddapi.payloads.requests.PostWithTopicRequest;
import com.openclassrooms.mddapi.payloads.responses.CommentResponse;
import com.openclassrooms.mddapi.payloads.responses.PostResponse;
import com.openclassrooms.mddapi.payloads.responses.SimpleOutputMessageResponse;
import com.openclassrooms.mddapi.security.services.JwtService;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.PostService;

import com.openclassrooms.mddapi.utils.DateFormatter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("posts")
public class PostController {

    final String bearerTokenString = "Bearer ";
    private final PostService postService;
    private final CommentService commentService;
    private final JwtService jwtService;

    public PostController(PostService postService, CommentService commentService, JwtService jwtService) {
        this.postService = postService;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    /**
     * Retrieve all posts
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var posts = postService.getAll();
        var postResponses = new ArrayList<PostResponse>();
        for (var post : posts) {
            var postResponse = ToPostResponse(post);
            postResponses.add(postResponse);
        }

        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }

    /**
     * Retrieve all posts for all topics subscribed (user feed)
     */
    @GetMapping(value = "/feed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPostBySubscribedTopics(@RequestHeader("Authorization") String authorizationHeader) {
        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        var posts = postService.getFeed(userFromToken);
        var postResponses = new ArrayList<PostResponse>();
        for (var post : posts) {
            var postResponse = ToPostResponse(post);
            postResponses.add(postResponse);
        }

        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }

    /**
     * Retrieve a post by id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostById(@PathVariable("id") final Integer id) {
        var post = postService.getById(id);

        var postResponse = ToPostResponse(post);

        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    /**
     * Post a post
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPost(@Valid @RequestParam final Integer topicId,
                                     @Valid @RequestBody PostWithTopicRequest postRequest,
                                     @RequestHeader("Authorization") String authorizationHeader) {

        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        postService.createFromRequest(topicId, userFromToken, postRequest);

        return new ResponseEntity<>(new SimpleOutputMessageResponse("Post has been created successfully !"), HttpStatus.OK);
    }

    /**
     * Post a comment for a given post
     */
    @PostMapping(value = "{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addComment(@PathVariable("postId") Integer postId,
                                        @Valid @RequestBody CommentPostRequest commentPostRequest,
                                        @RequestHeader("Authorization") String authorizationHeader) {

        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        commentService.createCommentFromRequest(postId, userFromToken, commentPostRequest);

        return new ResponseEntity<>(new SimpleOutputMessageResponse("Comment has been created successfully !"), HttpStatus.OK);
    }

    /**
     * Retrieve all comments for a given post
     */
    @GetMapping(value = "{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@PathVariable("postId") Integer postId) {
        var postComments = postService.getAllComments(postId);
        var commentsResponses = new ArrayList<CommentResponse>();
        for (var comment : postComments) {
            commentsResponses.add(CommentResponse.builder()
                    .id(comment.getId())
                    .username(comment.getOwner().getName())
                    .text(comment.getComment())
                    .createdAt(comment.getCreatedAt().format(DateFormatter.getFormatter()))
                    .build());
        }

        return new ResponseEntity<>(commentsResponses, HttpStatus.OK);
    }

    private PostResponse ToPostResponse(Post post) {
        if(post == null) {
            return new PostResponse();
        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .createdAt(post.getCreatedAt().format(DateFormatter.getFormatter()))
                .author(post.getOwner().getName())
                .topicId(post.getTopic().getId())
                .topicName(post.getTopic().getTitle())
                .build();
    }
}
