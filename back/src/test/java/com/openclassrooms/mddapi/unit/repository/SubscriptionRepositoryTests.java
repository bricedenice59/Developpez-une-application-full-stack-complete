package com.openclassrooms.mddapi.unit.repository;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.TopicRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Tag("SubscriptionRepositoryTests")
public class SubscriptionRepositoryTests {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    Topic topic1, topic2;
    User user1;
    Subscription subscription1, subscription2;

    @BeforeEach
    public void setUp() {
        topic1 = Topic.builder()
                .title("Test Topic 1")
                .description("Test description 1")
                .createdAt(LocalDateTime.now())
                .build();
        topic2 = Topic.builder()
                .title("Test Topic 1")
                .description("Test description 1")
                .createdAt(LocalDateTime.now())
                .build();

        topicRepository.save(topic1);
        topicRepository.save(topic2);

        user1 = User.builder()
                .name("test")
                .email("test@test.com")
                .createdAt(LocalDateTime.now())
                .password("")
                .build();

        user1 = userRepository.save(user1);

        subscription1 = Subscription.builder()
                .user(user1)
                .Topic(topic1)
                .createdAt(LocalDateTime.now())
                .build();
        subscription2 = Subscription.builder()
                .user(user1)
                .Topic(topic2)
                .createdAt(LocalDateTime.now())
                .build();

        subscription1 = subscriptionRepository.save(subscription1);
        subscription2 = subscriptionRepository.save(subscription2);
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(user1);
        topicRepository.delete(topic1);
        topicRepository.delete(topic2);

        subscriptionRepository.delete(subscription1);
        subscriptionRepository.delete(subscription2);
    }

    @Test
    public void testFindAllThemeIdsSubscribedByUser() {
        Optional<List<Integer>> result = subscriptionRepository.findAllThemeIdsSubscribedByUser(user1.getId());

        assertTrue(result.isPresent());
        assertTrue(result.get().contains(topic1.getId()));
        assertTrue(result.get().contains(topic2.getId()));
    }

    @Test
    public void testFindUniqueSubscriptionForThemeByUser() {
        Integer userId = user1.getId();
        Integer themeId = topic2.getId();

        Optional<Subscription> result = subscriptionRepository.findUniqueSubscriptionForThemeByUser(themeId, userId);

        assertTrue(result.isPresent());
        assertEquals(subscription2, result.get());
    }

    @Test
    public void testDeleteByThemeIdAndUserId() {
        Integer userId = user1.getId();
        Integer themeId = topic2.getId();

        subscriptionRepository.deleteByThemeIdAndUserId(themeId, userId);

        Optional<Subscription> result = subscriptionRepository.findUniqueSubscriptionForThemeByUser(themeId, userId);
        assertTrue(result.isEmpty());
    }
}
