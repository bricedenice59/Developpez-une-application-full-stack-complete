package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.models.Subscription;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    @Query("SELECT s.Topic.id FROM Subscription s WHERE s.user.id = :userId")
    Optional<List<Integer>> findAllThemeIdsSubscribedByUser(Integer userId);

    @Query("SELECT s FROM Subscription s WHERE s.Topic.id = :topicId AND s.user.id = :userId")
    Optional<Subscription> findUniqueSubscriptionForThemeByUser(Integer topicId, Integer userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Subscription s WHERE s.Topic.id = :topicId AND s.user.id = :userId")
    void deleteByThemeIdAndUserId(Integer topicId, Integer userId);
}

