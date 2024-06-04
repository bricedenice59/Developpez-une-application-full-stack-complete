package com.openclassrooms.mddapi.unit.repository;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
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
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    Theme theme1, theme2;
    User user1;
    Subscription subscription1, subscription2;

    @BeforeEach
    public void setUp() {
        theme1 = Theme.builder()
                .title("Test Theme 1")
                .description("Test description 1")
                .createdAt(LocalDateTime.now())
                .build();
        theme2 = Theme.builder()
                .title("Test Theme 1")
                .description("Test description 1")
                .createdAt(LocalDateTime.now())
                .build();

        themeRepository.save(theme1);
        themeRepository.save(theme2);

        user1 = User.builder()
                .name("test")
                .email("test@test.com")
                .createdAt(LocalDateTime.now())
                .password("")
                .build();

        user1 = userRepository.save(user1);

        subscription1 = Subscription.builder()
                .user(user1)
                .Theme(theme1)
                .createdAt(LocalDateTime.now())
                .build();
        subscription2 = Subscription.builder()
                .user(user1)
                .Theme(theme2)
                .createdAt(LocalDateTime.now())
                .build();

        subscription1 = subscriptionRepository.save(subscription1);
        subscription2 = subscriptionRepository.save(subscription2);
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(user1);
        themeRepository.delete(theme1);
        themeRepository.delete(theme2);

        subscriptionRepository.delete(subscription1);
        subscriptionRepository.delete(subscription2);
    }

    @Test
    public void testFindAllThemeIdsSubscribedByUser() {
        Optional<List<Integer>> result = subscriptionRepository.findAllThemeIdsSubscribedByUser(user1.getId());

        assertTrue(result.isPresent());
        assertTrue(result.get().contains(theme1.getId()));
        assertTrue(result.get().contains(theme2.getId()));
    }

    @Test
    public void testFindUniqueSubscriptionForThemeByUser() {
        Integer userId = user1.getId();
        Integer themeId = theme2.getId();

        Optional<Subscription> result = subscriptionRepository.findUniqueSubscriptionForThemeByUser(themeId, userId);

        assertTrue(result.isPresent());
        assertEquals(subscription2, result.get());
    }

    @Test
    public void testDeleteByThemeIdAndUserId() {
        Integer userId = user1.getId();
        Integer themeId = theme2.getId();

        subscriptionRepository.deleteByThemeIdAndUserId(themeId, userId);

        Optional<Subscription> result = subscriptionRepository.findUniqueSubscriptionForThemeByUser(themeId, userId);
        assertTrue(result.isEmpty());
    }
}
