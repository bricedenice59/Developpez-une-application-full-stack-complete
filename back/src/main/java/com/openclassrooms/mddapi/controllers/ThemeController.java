package com.openclassrooms.mddapi.controllers;


import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.payloads.responses.SimpleOutputMessageResponse;
import com.openclassrooms.mddapi.payloads.responses.ThemeResponse;
import com.openclassrooms.mddapi.security.services.JwtService;
import com.openclassrooms.mddapi.services.SubscriptionService;
import com.openclassrooms.mddapi.services.ThemeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@RestController
@RequestMapping("theme")
public class ThemeController {

    final String bearerTokenString = "Bearer ";
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final JwtService jwtService;
    private final ThemeService themeService;
    private final SubscriptionService themeSubscriptionService ;

    public ThemeController(ThemeService themeService, JwtService jwtService, SubscriptionService themeSubscriptionService) {
        this.jwtService = jwtService;
        this.themeService = themeService;
        this.themeSubscriptionService = themeSubscriptionService;
    }

    /**
     * Retrieve all themes
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var themes = themeService.getAll();
        var themeResponses = new ArrayList<ThemeResponse>();
        for (var theme : themes) {
            var themeResponse = ToPostResponse(theme);
            themeResponses.add(themeResponse);
        }

        return new ResponseEntity<>(themeResponses, HttpStatus.OK);
    }

    /**
     * Retrieve all themes subscribed for a given user
     */
    @GetMapping(value = "/subscribed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSubscribed(@RequestHeader("Authorization") String authorizationHeader) {
        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        var allSubscriptions = themeSubscriptionService.getAllSubscribed(userFromToken);
        var themeResponses = new ArrayList<ThemeResponse>();
        if(!allSubscriptions.isEmpty()) {
            for (var themeId : allSubscriptions) {
                var themeResponse = ToPostResponse(themeService.getById(themeId));
                themeResponses.add(themeResponse);
            }
        }

        return new ResponseEntity<>(themeResponses, HttpStatus.OK);
    }

    /**
     * Subscribe or Unsubscribe to/from a theme for a given user
     */
    @PostMapping(value = "/subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> subscribeOrUnsubscribeThemeForUser(@RequestParam final Integer themeId,
                                     @RequestParam final Boolean subscribe,
                                     @RequestHeader("Authorization") String authorizationHeader) {

        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        themeSubscriptionService.ManageSubscription(subscribe, userFromToken, themeId);

        var message = subscribe
                ? new SimpleOutputMessageResponse("User has subscribed successfully to theme!")
                : new SimpleOutputMessageResponse("User has unsubscribed successfully to theme!");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    private ThemeResponse ToPostResponse(Theme theme) {
        if(theme == null) {
            return new ThemeResponse();
        }

        return ThemeResponse.builder()
                .id(theme.getId())
                .title(theme.getTitle())
                .description(theme.getDescription())
                .build();
    }
}
