package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
//    SELECT posts.* from posts
//    INNER JOIN topics t on t.id = posts.topic_id
//    INNER JOIN subscriptions s on s.topic_id = t.id
//    WHERE s.user_id = ...
    @Query("SELECT p FROM Post p INNER JOIN p.topic t INNER JOIN Subscription s ON s.Topic = t WHERE s.user.id = :userId")
    List<Post> findPostsBySubscriptionUserId(@Param("userId") Integer userId);
}
