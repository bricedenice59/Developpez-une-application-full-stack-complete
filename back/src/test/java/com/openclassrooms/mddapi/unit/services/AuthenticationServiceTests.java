package com.openclassrooms.mddapi.unit.services;

import com.openclassrooms.mddapi.exceptions.UserAlreadyExistException;
import com.openclassrooms.mddapi.models.Role;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.models.requests.LoginUserRequest;
import com.openclassrooms.mddapi.models.requests.RegisterUserRequest;
import com.openclassrooms.mddapi.models.responses.AuthenticationResponse;
import com.openclassrooms.mddapi.repositories.RoleRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.security.services.JwtService;
import com.openclassrooms.mddapi.services.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Test register a new user should be successful")
    public void AuthenticationServiceTests_registerUser_ShouldSuccessfullyCreateUseInDatabase() {
        var registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("test@test.com");
        registerUserRequest.setPassword("password!");
        registerUserRequest.setName("name");

        var role = Role.builder()
                .id(1)
                .name("USER")
                .createdAt(LocalDateTime.now())
            .build();

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn("encodedPassword");

        authenticationService.registerUser(registerUserRequest);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test register a new user that already exist with his email in system should throw UserAlreadyExistException")
    public void AuthenticationServiceTests_registerUser_ShouldThrowUserAlreadyExistException() {
        var registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("test@test.com");
        registerUserRequest.setPassword("password!");
        registerUserRequest.setName("test");

        var role = Role.builder()
                    .id(1)
                    .name("USER")
                    .createdAt(LocalDateTime.now())
                .build();

        var user = User.builder()
                    .id(1)
                    .email("test@test.com")
                    .password("password!")
                .build();

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistException.class, () -> authenticationService.registerUser(registerUserRequest));
    }

    @Test
    @DisplayName("Test login a user should be successful and return with a valid AuthenticationResponse object")
    void AuthenticationServiceTests_registerUser_ShouldReturnAuthenticationResponse() {
        var user = User.builder()
                .id(1)
                .email("test@test.com")
                .password("password!")
                .build();

        var loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("test@test.com");
        loginUserRequest.setPassword("password!");

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(mockAuth.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(anyMap(), eq(user))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.loginUser(loginUserRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    @DisplayName("Test login a user with wrong login/password input should throw a BadCredentialsException")
    void AuthenticationServiceTests_loginUser_ShouldThrowBadCredentialsException() {
        var loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("bla@blabla.com");
        loginUserRequest.setPassword("blablabla!");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.loginUser(loginUserRequest));
    }
}
