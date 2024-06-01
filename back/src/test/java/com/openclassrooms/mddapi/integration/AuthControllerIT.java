package com.openclassrooms.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.openclassrooms.mddapi.models.requests.LoginUserRequest;
import com.openclassrooms.mddapi.models.requests.RegisterUserRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private final String email = "test@test.com";
    private final String wellFormatedPassword = "paSSword1!";

    @Test
    @Order(1)
    @Tag("api/auth/register")
    @DisplayName("it should fail to register a user with a broken password constraint")
    void AuthController_registerUser_WithIncorrectPasswordValidation_ShouldReturnBadRequestMessageResponse() throws Exception {
        var signUpRequest = new RegisterUserRequest();

        signUpRequest.setEmail(email);
        String wrongFormatedPassword = "password";
        signUpRequest.setPassword(wrongFormatedPassword);
        signUpRequest.setName("toto");

        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(signUpRequest);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Invalid password format, at least one digit, one lowercase letter, one uppercase letter and one special character are required!"));
    }

    @Test
    @Order(2)
    @Tag("api/auth/register")
    @DisplayName("it should register the user successfully")
    void AuthController_registerUser_WithCorrectPasswordValidation_ShouldReturnMessageResponse() throws Exception {
        var signUpRequest = new RegisterUserRequest();

        signUpRequest.setEmail(email);
        signUpRequest.setPassword(wellFormatedPassword);
        signUpRequest.setName("toto");

        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(signUpRequest);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully !"));
    }

    @Test
    @Order(3)
    @Tag("api/auth/register")
    @DisplayName("it should fail to register a user who has already registered with a same email address")
    void AuthController_registerUser_Again_ShouldReturnBadRequestMessageResponse() throws Exception {
        var signUpRequest = new RegisterUserRequest();

        signUpRequest.setEmail(email);
        signUpRequest.setPassword(wellFormatedPassword);
        signUpRequest.setName("toto");

        var objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(signUpRequest);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(MessageFormat.format("User with email {0} already exists", email)));
    }

    @Test
    @Order(4)
    @Tag("api/auth/login")
    @DisplayName("it should login the user successfully")
    void AuthController_loginUser_ShouldReturnIsOK() throws Exception {
        var loginRequest = new LoginUserRequest();

        loginRequest.setEmail(email);
        loginRequest.setPassword(wellFormatedPassword);

        ObjectMapper objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
