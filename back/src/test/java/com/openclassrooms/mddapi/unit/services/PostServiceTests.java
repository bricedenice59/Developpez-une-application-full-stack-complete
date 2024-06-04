package com.openclassrooms.mddapi.unit.services;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.mddapi.exceptions.*;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payloads.requests.PostWithThemeRequest;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.services.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private PostService postService;


    @Test
    @DisplayName("Test retrieve all posts should be successful")
    public void PostServiceTests_getAll_ShouldReturnAListOfPosts() {
        Post post1 = Post.builder()
                .id(1)
                .title("my post 1")
                .description("my description 1")
                .createdAt(LocalDateTime.now())
                .build();
        Post post2 = Post.builder()
                .id(2)
                .title("my post 2")
                .description("my description 2")
                .createdAt(LocalDateTime.now())
                .build();

        List<Post> expectedPosts = Arrays.asList(post1, post2);
        when(postRepository.findAll()).thenReturn(expectedPosts);

        List<Post> actualPosts = postService.getAll();

        assertEquals(expectedPosts, actualPosts);
        verify(postRepository).findAll();
    }

    @Test
    @DisplayName("Test retrieve a post should be successful")
    public void PostServiceTests_getById_ShouldReturnAListOfPosts() {
        var postId = 1;
        Post post = Post.builder()
                .id(postId)
                .title("my post 1")
                .description("my description 1")
                .createdAt(LocalDateTime.now())
                .build();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Post actualPost = postService.getById(postId);

        assertEquals(post, actualPost);
        verify(postRepository).findById(postId);
    }

    @Test
    @DisplayName("Test retrieve a post with a non existent id should fail and throw a PostNotFoundException")
    public void PostServiceTests_getById_ShouldThrowAPostNotFoundException() {
        Integer postId = 99;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getById(postId));
    }

    @Test
    @DisplayName("Test create a new post should be successful")
    public void PostServiceTests_createFromRequest_ShouldSuccessfullySaveComment() {
        Integer themeId = 1;
        String userEmail = "test@example.com";
        PostWithThemeRequest postRequest = new PostWithThemeRequest();
        postRequest.setTitle("Test Title");
        postRequest.setDescription("Test Description");

        User mockUser = new User();
        mockUser.setEmail(userEmail);
        Theme mockTheme = new Theme();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(themeRepository.findById(themeId)).thenReturn(Optional.of(mockTheme));
        when(postRepository.save(any(Post.class))).thenReturn(new Post());

        postService.createFromRequest(themeId, userEmail, postRequest);

        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("Test create a new post with a non existent user should fail and throw a UserNotFoundException")
    public void PostServiceTests_createFromRequest_WithNonExistentUser_ShouldThrowAPostNotFoundException() {
        Integer themeId = 1;
        String userEmail = "nonexistent@example.com";
        PostWithThemeRequest postRequest = new PostWithThemeRequest();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> postService.createFromRequest(themeId, userEmail, postRequest));
    }

    @Test
    @DisplayName("Test create a new post with a non existent theme should fail and throw a ThemeNotFoundException")
    public void PostServiceTests_createFromRequest_WithNonExistentTheme_ShouldThrowAThemeNotFoundException() {
        Integer themeId = 99;
        String userEmail = "test@example.com";
        PostWithThemeRequest postRequest = new PostWithThemeRequest();

        User mockUser = new User();
        mockUser.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(themeRepository.findById(themeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ThemeNotFoundException.class, () -> postService.createFromRequest(themeId, userEmail, postRequest));
    }
}
