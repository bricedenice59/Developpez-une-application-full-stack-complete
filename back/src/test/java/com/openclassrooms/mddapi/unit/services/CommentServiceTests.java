package com.openclassrooms.mddapi.unit.services;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.mddapi.exceptions.PostNotFoundException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payloads.requests.CommentPostRequest;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.services.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("Test create a new comment should be successful")
    public void CommentServiceTests_CreateCommentFromRequest_ShouldSuccessfullySaveComment() {
        Integer postId = 1;
        String userEmail = "test@example.com";
        CommentPostRequest commentPostRequest = new CommentPostRequest();
        commentPostRequest.setComment("This is a test comment");

        User mockUser = new User();
        mockUser.setEmail(userEmail);
        Post mockPost = new Post();
        Comment mockComment = new Comment();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        commentService.createCommentFromRequest(postId, userEmail, commentPostRequest);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("Test create a new comment from a non existent user should fail and throw a UserNotFoundException")
    public void CommentServiceTests_CreateCommentFromRequestWithNonExistentUser_ShouldThrowUserNotFoundException() {
        Integer postId = 1;
        String userEmail = "nonexistent@example.com";
        CommentPostRequest commentPostRequest = new CommentPostRequest();
        commentPostRequest.setComment("This is a test comment");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> commentService.createCommentFromRequest(postId, userEmail, commentPostRequest));
    }

    @Test
    @DisplayName("Test create a new comment from a non existent post should fail and throw a PostNotFoundException")
    public void CommentServiceTests_CreateCommentFromRequestWithNonExistentPost_ShouldThrowPostNotFoundException() {
        Integer postId = 99;
        String userEmail = "test@example.com";
        CommentPostRequest commentPostRequest = new CommentPostRequest();
        commentPostRequest.setComment("This is a test comment");

        User mockUser = new User();
        mockUser.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> commentService.createCommentFromRequest(postId, userEmail, commentPostRequest));
    }
}
