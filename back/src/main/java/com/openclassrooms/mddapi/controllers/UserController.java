package com.openclassrooms.mddapi.controllers;


import com.openclassrooms.mddapi.payloads.requests.UpdateUserDetailsRequest;
import com.openclassrooms.mddapi.payloads.responses.ApiErrorResponse;
import com.openclassrooms.mddapi.payloads.responses.SimpleOutputMessageResponse;
import com.openclassrooms.mddapi.payloads.responses.UserResponse;
import com.openclassrooms.mddapi.security.services.JwtService;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Get a user details by id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserDetails(@PathVariable("id") final Integer id) {
        var user = userService.getUserById(id);

        var userResponse = UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    /**
     * Updates a user profile details by id
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDetails(@PathVariable("id") Integer id,
                                           @Valid @RequestBody UpdateUserDetailsRequest userDetailsRequest,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        final String bearerTokenString = "Bearer ";
        var jwtToken = authorizationHeader.substring(bearerTokenString.length());

        var userFromToken = jwtService.extractUserName(jwtToken);
        var userFromDatabase = userService.getUserById(id);

        if(!userFromToken.equals(userFromDatabase.getEmail())) {
            var errorApiResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), "You are not allowed to update details for this user !", LocalDateTime.now());
            return new ResponseEntity<>(errorApiResponse, HttpStatus.UNAUTHORIZED) ;
        }

        userService.update(id, !userFromDatabase.getEmail().equals(userDetailsRequest.getEmail()), userDetailsRequest);

        return new ResponseEntity<>(new SimpleOutputMessageResponse("User details have been updated successfully !"), HttpStatus.OK);
    }
}
