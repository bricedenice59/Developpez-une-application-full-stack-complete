package com.openclassrooms.mddapi.controllers;


import com.openclassrooms.mddapi.payloads.requests.LoginUserRequest;
import com.openclassrooms.mddapi.payloads.requests.RegisterUserRequest;
import com.openclassrooms.mddapi.payloads.responses.AuthenticationResponse;
import com.openclassrooms.mddapi.payloads.responses.SimpleOutputMessageResponse;
import com.openclassrooms.mddapi.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Post - Register a user
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        authenticationService.registerUser(registerUserRequest);
        return new ResponseEntity<>(new SimpleOutputMessageResponse("User registered successfully !"), HttpStatus.OK);
    }

    /**
     * Post - Login a user
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        var authenticationResponse = authenticationService.loginUser(loginUserRequest);
        return ResponseEntity.ok(authenticationResponse);
    }
}
