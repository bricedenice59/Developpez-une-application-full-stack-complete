package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.exceptions.PostNotFoundException;
import com.openclassrooms.mddapi.exceptions.TopicNotFoundException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.payloads.requests.PostWithTopicRequest;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
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
    private final TopicRepository topicRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, TopicRepository topicRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.commentRepository = commentRepository;
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public List<Post> getFeed(final String userEmail) {
        var user = userRepository.findByEmail(userEmail);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return postRepository.findPostsBySubscriptionUserId(user.get().getId());
    }

    public Post getById(final Integer id) {
        var post = postRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    public void createFromRequest(final Integer ThemeId, final String userEmail, final PostWithTopicRequest postRequest) {
        var owner = userRepository.findByEmail(userEmail);
        if(owner.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        var theme = topicRepository.findById(ThemeId);
        if(theme.isEmpty()) {
            throw new TopicNotFoundException("Topic not found");
        }

        var post = Post.builder()
                .title(postRequest.getTitle())
                .description(postRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .topic(theme.get())
                .owner(owner.get())
                .build();

        postRepository.save(post);
    }

    public List<Comment> getAllComments(Integer postId) {
        return commentRepository.findByPostId(postId);
    }
}
