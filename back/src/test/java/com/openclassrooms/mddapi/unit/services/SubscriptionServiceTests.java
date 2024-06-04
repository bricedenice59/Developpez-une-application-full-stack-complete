package com.openclassrooms.mddapi.unit.services;

import com.openclassrooms.mddapi.exceptions.ThemeSubscriptionException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.services.SubscriptionService;
import com.openclassrooms.mddapi.services.ThemeService;
import com.openclassrooms.mddapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private ThemeService themeService;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void testGetAllSubscribed() {
        String userEmail = "test@example.com";
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail(userEmail);

        List<Integer> expectedSubscriptions = Arrays.asList(1, 2,3);
        when(userService.getByEmail(userEmail)).thenReturn(mockUser);
        when(subscriptionRepository.findAllThemeIdsSubscribedByUser(mockUser.getId())).thenReturn(Optional.of(expectedSubscriptions));

        List<Integer> subscribedThemeIds = subscriptionService.getAllSubscribed(userEmail);

        assertEquals(expectedSubscriptions, subscribedThemeIds);
        verify(subscriptionRepository).findAllThemeIdsSubscribedByUser(mockUser.getId());
    }

    @Test
    public void SubscriptionServiceTests_ManageSubscription_Subscribe_WithNoExistingPreviousSubscription_ShouldBeSuccessful() {
        String userEmail = "test@example.com";
        Integer themeId = 1;

        User mockUser = new User();
        mockUser.setId(1);

        Theme mockTheme = new Theme();
        mockTheme.setId(themeId);

        when(userService.getByEmail(userEmail)).thenReturn(mockUser);
        when(themeService.getById(themeId)).thenReturn(mockTheme);
        when(subscriptionRepository.findUniqueSubscriptionForThemeByUser(themeId, mockUser.getId())).thenReturn(Optional.empty());

        subscriptionService.ManageSubscription(true, userEmail, themeId);

        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    public void SubscriptionServiceTests_ManageSubscription_Subscribe_WithAnExistingPreviousSubscription_ShouldThrowAThemeSubscriptionException() {
        String userEmail = "test@example.com";
        Integer themeId = 1;

        User mockUser = new User();
        mockUser.setId(1);

        Theme mockTheme = new Theme();
        mockTheme.setId(themeId);

        Subscription mockSubscription = new Subscription();
        mockSubscription.setId(1);

        when(userService.getByEmail(userEmail)).thenReturn(mockUser);
        when(themeService.getById(themeId)).thenReturn(mockTheme);
        when(subscriptionRepository.findUniqueSubscriptionForThemeByUser(themeId, mockUser.getId())).thenReturn(Optional.of(mockSubscription));

        assertThrows(ThemeSubscriptionException.class, () -> subscriptionService.ManageSubscription(true, userEmail, themeId));
    }

    @Test
    public void SubscriptionServiceTests_ManageSubscription_UnSubscribe_WithNoExistingPreviousSubscription_ShouldThrowAThemeSubscriptionException() {
        String userEmail = "test@example.com";
        Integer themeId = 1;

        User mockUser = new User();
        mockUser.setId(1);

        Theme mockTheme = new Theme();
        mockTheme.setId(themeId);


        when(userService.getByEmail(userEmail)).thenReturn(mockUser);
        when(themeService.getById(themeId)).thenReturn(mockTheme);
        when(subscriptionRepository.findUniqueSubscriptionForThemeByUser(themeId, mockUser.getId())).thenReturn(Optional.empty());

        // Call the service method
        assertThrows(ThemeSubscriptionException.class, () -> subscriptionService.ManageSubscription(false, userEmail, themeId));
    }

    @Test
    public void SubscriptionServiceTests_ManageSubscription_UnSubscribe_WithAnExistingPreviousSubscription_ShouldBeSuccessful() {
        String userEmail = "test@example.com";
        Integer themeId = 1;

        User mockUser = new User();
        mockUser.setId(1);

        Theme mockTheme = new Theme();
        mockTheme.setId(themeId);

        Subscription mockSubscription = new Subscription();
        mockSubscription.setId(1);

        when(userService.getByEmail(userEmail)).thenReturn(mockUser);
        when(themeService.getById(themeId)).thenReturn(mockTheme);
        when(subscriptionRepository.findUniqueSubscriptionForThemeByUser(themeId, mockUser.getId())).thenReturn(Optional.of(mockSubscription));

        subscriptionService.ManageSubscription(false, userEmail, themeId);

        verify(subscriptionRepository).deleteByThemeIdAndUserId(themeId, mockUser.getId());
    }
}
