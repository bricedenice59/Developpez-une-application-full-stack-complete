package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.exceptions.ThemeSubscriptionException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class SubscriptionService {

    private final UserService userService;
    private final TopicService topicService;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(UserService userService, TopicService topicService, SubscriptionRepository subscriptionRepository) {
        this.userService = userService;
        this.topicService = topicService;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Integer> getAllSubscribed(final String userEmail) {
        var user = userService.getByEmail(userEmail);

        var topicIds = subscriptionRepository.findAllThemeIdsSubscribedByUser(user.getId());
        return topicIds.orElse(new ArrayList<>());
    }

    public void ManageSubscription(boolean subscribe, final String userEmail, final Integer topicId){
        var user = userService.getByEmail(userEmail);
        var topic = topicService.getById(topicId);
        var userId = user.getId();

        Optional<Subscription> subscription = subscriptionRepository.findUniqueSubscriptionForThemeByUser(topic.getId(), userId);
        if(!subscribe){
            //delete subscription that matches a tuple topic/user
            if (subscription.isPresent()) {
                subscriptionRepository.deleteByThemeIdAndUserId(topic.getId(), userId);
            }
            else {
                throw new ThemeSubscriptionException("Impossible to unsubscribe to a topic with id = " + topic.getId() + " " + "No subscription found");
            }
        }
        else {
            if(subscription.isPresent()){
                throw new ThemeSubscriptionException("Impossible to subscribe to a topic with id = " + topic.getId() +  " " +"Subscription already exist");
            }
            var newSubscription = Subscription
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .Topic(topic)
                    .user(user)
                    .build();
            subscriptionRepository.save(newSubscription);
        }
    }
}
