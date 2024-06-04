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
    private final ThemeService themeService;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(UserService userService, ThemeService themeService, SubscriptionRepository subscriptionRepository) {
        this.userService = userService;
        this.themeService = themeService;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Integer> getAllSubscribed(final String userEmail) {
        var user = userService.getByEmail(userEmail);

        var themeIds = subscriptionRepository.findAllThemeIdsSubscribedByUser(user.getId());
        return themeIds.orElse(new ArrayList<>());
    }

    public void ManageSubscription(boolean subscribe, final String userEmail, final Integer theme_id){
        var user = userService.getByEmail(userEmail);
        var theme = themeService.getById(theme_id);
        var userId = user.getId();

        Optional<Subscription> subscription = subscriptionRepository.findUniqueSubscriptionForThemeByUser(theme.getId(), userId);
        if(!subscribe){
            //delete subscription that matches a tuple theme/user
            if (subscription.isPresent()) {
                subscriptionRepository.deleteByThemeIdAndUserId(theme.getId(), userId);
            }
            else {
                throw new ThemeSubscriptionException("Impossible to unsubscribe to a theme with id = " + theme.getId() + " " + "No subscription found");
            }
        }
        else {
            if(subscription.isPresent()){
                throw new ThemeSubscriptionException("Impossible to subscribe to a theme with id = " + theme.getId() +  " " +"Subscription already exist");
            }
            var newSubscription = Subscription
                    .builder()
                    .createdAt(LocalDateTime.now())
                    .Theme(theme)
                    .user(user)
                    .build();
            subscriptionRepository.save(newSubscription);
        }
    }
}
