package com.openclassrooms.mddapi.controllers;


import com.openclassrooms.mddapi.models.Topic;
import com.openclassrooms.mddapi.payloads.responses.SimpleOutputMessageResponse;
import com.openclassrooms.mddapi.payloads.responses.TopicResponse;
import com.openclassrooms.mddapi.security.services.JwtService;
import com.openclassrooms.mddapi.services.SubscriptionService;
import com.openclassrooms.mddapi.services.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("topics")
public class TopicController {

    final String bearerTokenString = "Bearer ";
    private final JwtService jwtService;
    private final TopicService topicService;
    private final SubscriptionService topicsSubscriptionService;

    public TopicController(TopicService topicService, JwtService jwtService, SubscriptionService topicsSubscriptionService) {
        this.jwtService = jwtService;
        this.topicService = topicService;
        this.topicsSubscriptionService = topicsSubscriptionService;
    }

    /**
     * Retrieve all topics
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var topics = topicService.getAll();
        var topicsResponses = new ArrayList<TopicResponse>();
        for (var topic : topics) {
            var topicResponse = ToPostResponse(topic);
            topicsResponses.add(topicResponse);
        }

        return new ResponseEntity<>(topicsResponses, HttpStatus.OK);
    }

    /**
     * Retrieve all topics subscribed for a given user
     */
    @GetMapping(value = "/subscribed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSubscribed(@RequestHeader("Authorization") String authorizationHeader) {
        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        var allSubscriptions = topicsSubscriptionService.getAllSubscribed(userFromToken);
        var topicsResponses = new ArrayList<TopicResponse>();
        if(!allSubscriptions.isEmpty()) {
            for (var topicId : allSubscriptions) {
                var topicResponse = ToPostResponse(topicService.getById(topicId));
                topicsResponses.add(topicResponse);
            }
        }

        return new ResponseEntity<>(topicsResponses, HttpStatus.OK);
    }

    /**
     * Subscribe or Unsubscribe to/from a topic for a given user
     */
    @PostMapping(value = "/subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> subscribeOrUnsubscribeTopicForUser(@RequestParam final Integer topicId,
                                     @RequestParam final Boolean subscribe,
                                     @RequestHeader("Authorization") String authorizationHeader) {

        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        topicsSubscriptionService.ManageSubscription(subscribe, userFromToken, topicId);

        var message = subscribe
                ? new SimpleOutputMessageResponse("User has subscribed successfully to topic!")
                : new SimpleOutputMessageResponse("User has unsubscribed successfully to topic!");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    private TopicResponse ToPostResponse(Topic topic) {
        if(topic == null) {
            return new TopicResponse();
        }

        return TopicResponse.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .build();
    }
}
