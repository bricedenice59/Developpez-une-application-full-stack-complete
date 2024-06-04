package com.openclassrooms.mddapi.controllers;


import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.payloads.requests.CommentPostRequest;
import com.openclassrooms.mddapi.payloads.requests.PostWithThemeRequest;
import com.openclassrooms.mddapi.payloads.responses.CommentResponse;
import com.openclassrooms.mddapi.payloads.responses.PostResponse;
import com.openclassrooms.mddapi.payloads.responses.SimpleOutputMessageResponse;
import com.openclassrooms.mddapi.security.services.JwtService;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.PostService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@RestController
@RequestMapping("posts")
public class PostController {

    final String bearerTokenString = "Bearer ";
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
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
    public ResponseEntity<?> addPost(@Valid @RequestParam final Integer themeId,
                                     @Valid @RequestBody PostWithThemeRequest postRequest,
                                     @RequestHeader("Authorization") String authorizationHeader) {

        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        postService.createFromRequest(themeId, userFromToken, postRequest);

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

    private PostResponse ToPostResponse(Post post) {
        if(post == null) {
            return new PostResponse();
        }

        var commentsResponses = new ArrayList<CommentResponse>();
        for (var comment : post.getComments()) {
            commentsResponses.add(CommentResponse.builder()
                    .id(comment.getId())
                    .username(comment.getOwner().getName())
                    .text(comment.getComment())
                    .createdAt(comment.getCreatedAt().format(formatter))
                    .build());
        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .createdAt(post.getCreatedAt().format(formatter))
                .author(post.getOwner().getName())
                .themeId(post.getTheme().getId())
                .comments(commentsResponses)
                .build();
    }
}
