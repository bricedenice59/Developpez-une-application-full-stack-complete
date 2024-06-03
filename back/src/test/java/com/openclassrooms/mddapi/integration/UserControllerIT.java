package com.openclassrooms.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;

import com.openclassrooms.mddapi.payloads.requests.LoginUserRequest;
import com.openclassrooms.mddapi.payloads.requests.RegisterUserRequest;
import com.openclassrooms.mddapi.payloads.requests.UpdateUserDetailsRequest;
import com.openclassrooms.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Tag("api/user")
public class UserControllerIT extends BaseIT {

    @Autowired
    public UserControllerIT(UserRepository userRepository) {
        super(userRepository);
    }

    //region Test unauthorized endpoints

    @Test
    @DisplayName("it should fail to find a user when no authorization is provided")
    public void UserController_getUserById_No_AuthorizationProvided_ShouldReturnUnauthorizedResponse() throws Exception {
        mockMvc.perform(get("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("it should fail to update a user details when no authorization is provided")
    public void UserController_updateDetailsById_No_AuthorizationProvided_ShouldReturnUnauthorizedResponse() throws Exception {
        var userDetailsRequest = UpdateUserDetailsRequest.builder()
                        .email("email@email.com")
                        .name("toto")
                    .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDetailsRequest)))
                .andExpect(status().isUnauthorized());
    }

    //endregion

    @Test
    @DisplayName("it should fail to retrieve a user with an invalid formatted input user id")
    @WithUserDetails(value = "brice@denice.com")
    public void UserController_getUserById_ShouldReturnBadRequestResponse() throws Exception {
        var userId = "aa"; //invalid id

        mockMvc.perform(get("/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("it should fail to retrieve a user for an unknown user")
    @WithUserDetails(value = "brice@denice.com")
    public void UserController_getUserById_ShouldReturnNotFoundResponse() throws Exception {
        Integer userId = 55; //this user does not exist

        mockMvc.perform(get("/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("it should retrieve a user successfully")
    @WithUserDetails(value = "brice@denice.com")
    public void UserController_getUserById_ShouldReturnOkResponse() throws Exception {
        mockMvc.perform(get("/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty());
    }

    @Test
    @DisplayName("it should fail as current logged user is not allowed to update any other user details")
    public void UserController_updateDetailsById_ShouldReturnUnauthorizedResponse() throws Exception {
        var email = "email@email.com";
        var password = "password!!34F";
        var name = "toto";

        var emailUpdated = "email111@email.com";
        var nameUpdated = "nameUpdated";

        //register a new user
        var signUpRequest = new RegisterUserRequest();

        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setName(name);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isOk());

        //login user
        var loginRequest = new LoginUserRequest();

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        //need to log in first to extract the auth token
        var result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var userToken = JsonPath.parse(response).read("$.token");

        var userDetailsRequest = UpdateUserDetailsRequest.builder()
                .email(emailUpdated)
                .name(nameUpdated)
                .build();

        //try to update a user that is not the one logged
        mockMvc.perform(MockMvcRequestBuilders.put("/user/{id}", user.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDetailsRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage").value("You are not allowed to update details for this user !"));
    }

    @Test
    @DisplayName("it should successfully update a user details if logged user updates its own details")
    public void UserController_updateDetailsById_ShouldReturnOkResponse() throws Exception {
        var email = "email@email111.com";
        var password = "password!!34F";
        var name = "toto";

        var emailUpdated = "email111@email111.com";
        var nameUpdated = "nameUpdated";

        //register a new user
        var signUpRequest = new RegisterUserRequest();

        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setName(name);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isOk());

        //login user
        var loginRequest = new LoginUserRequest();

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        //need to log in first to extract the auth token
        var result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var userToken = JsonPath.parse(response).read("$.token");

        //get the new created user id
        var newlyCreatedUser = userRepository.findByEmail(email);
        if(newlyCreatedUser.isEmpty()) {
            throw new UserNotFoundException("At that stage, a user with the email address '" + email + "' should exist in database");
        }

        var userDetailsRequest = UpdateUserDetailsRequest.builder()
                .email(emailUpdated)
                .name(nameUpdated)
                .build();

        //try to update a user that is not the one logged
        mockMvc.perform(MockMvcRequestBuilders.put("/user/{id}", newlyCreatedUser.get().getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDetailsRequest)))
                .andExpect(status().isOk());
    }
}
