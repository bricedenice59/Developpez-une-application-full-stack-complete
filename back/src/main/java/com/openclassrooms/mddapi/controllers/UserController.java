package com.openclassrooms.mddapi.controllers;


import com.openclassrooms.mddapi.payloads.requests.UpdateUserDetailsRequest;
import com.openclassrooms.mddapi.payloads.responses.SimpleOutputMessageResponse;
import com.openclassrooms.mddapi.payloads.responses.UserResponse;
import com.openclassrooms.mddapi.security.services.JwtService;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    final String bearerTokenString = "Bearer ";
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Get a user details
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        var jwtToken = authorizationHeader.substring(bearerTokenString.length());
        var userFromToken = jwtService.extractUserName(jwtToken);

        var user = userService.getByEmail(userFromToken);

        var userResponse = UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    /**
     * Updates a user profile details
     */
    @PutMapping("")
    public ResponseEntity<?> updateDetails(@Valid @RequestBody UpdateUserDetailsRequest userDetailsRequest,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        final String bearerTokenString = "Bearer ";
        var jwtToken = authorizationHeader.substring(bearerTokenString.length());

        var userFromToken = jwtService.extractUserName(jwtToken);
        var user = userService.getByEmail(userFromToken);

        userService.updateFromRequest(user, !user.getEmail().equals(userDetailsRequest.getEmail()), userDetailsRequest);

        return new ResponseEntity<>(new SimpleOutputMessageResponse("User details have been updated successfully !"), HttpStatus.OK);
    }
}
