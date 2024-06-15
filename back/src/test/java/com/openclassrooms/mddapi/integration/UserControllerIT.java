package com.openclassrooms.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import com.openclassrooms.mddapi.payloads.requests.LoginUserRequest;
import com.openclassrooms.mddapi.payloads.requests.RegisterUserRequest;
import com.openclassrooms.mddapi.payloads.requests.UpdateUserDetailsRequest;
import com.openclassrooms.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    public String createUserAndLogin_ThenReturnsJwtToken(String email, String password, String name) throws Exception {
        var signUpRequest = new RegisterUserRequest();

        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setName(name);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully !"));

        //login user
        var loginRequest = new LoginUserRequest();

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        //need to log in to extract the auth token
        var result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.parse(response).read("$.token");
    }

    //region Test unauthorized endpoints

    @Test
    @DisplayName("it should fail to find a user when no authorization is provided")
    public void UserController_getUserDetails_No_AuthorizationProvided_ShouldReturnUnauthorizedResponse() throws Exception {
        mockMvc.perform(get("/user", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("it should fail to update a user details when no authorization is provided")
    public void UserController_updateDetails_No_AuthorizationProvided_ShouldReturnUnauthorizedResponse() throws Exception {
        var userDetailsRequest = UpdateUserDetailsRequest.builder()
                        .email("email@email.com")
                        .name("toto")
                    .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDetailsRequest)))
                .andExpect(status().isUnauthorized());
    }

    //endregion

    @Test
    @DisplayName("it should retrieve a user successfully")
    public void UserController_getUserDetail_ShouldReturnOkResponse() throws Exception {
        var userToken = createUserAndLogin_ThenReturnsJwtToken("email@email.com", "password!4Z", "name");
        mockMvc.perform(get("/user")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty());
    }

    @Test
    @DisplayName("it should successfully update a user details")
    public void UserController_updateDetails_ShouldReturnOkResponse() throws Exception {
        var userToken = createUserAndLogin_ThenReturnsJwtToken("email@email1.com", "password!4W", "name");
        var emailUpdated = "email111@email111.com";
        var nameUpdated = "nameUpdated";

        var userDetailsRequest = UpdateUserDetailsRequest.builder()
                .email(emailUpdated)
                .name(nameUpdated)
                .build();

        //try to update a user that is not the one logged
        mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDetailsRequest)))
                .andExpect(status().isOk());
    }
}
