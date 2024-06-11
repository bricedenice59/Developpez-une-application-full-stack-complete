package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.exceptions.PostNotFoundException;
import com.openclassrooms.mddapi.exceptions.ThemeNotFoundException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.payloads.requests.PostWithThemeRequest;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, ThemeRepository themeRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
        this.commentRepository = commentRepository;
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post getById(final Integer id) {
        var post = postRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    public void createFromRequest(final Integer ThemeId, final String userEmail, final PostWithThemeRequest postRequest) {
        var owner = userRepository.findByEmail(userEmail);
        if(owner.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        var theme = themeRepository.findById(ThemeId);
        if(theme.isEmpty()) {
            throw new ThemeNotFoundException("Theme not found");
        }

        var post = Post.builder()
                .title(postRequest.getTitle())
                .description(postRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .theme(theme.get())
                .owner(owner.get())
                .build();

        postRepository.save(post);
    }

    public List<Comment> getAllComments(Integer postId) {
        return commentRepository.findByPostId(postId);
    }
}
