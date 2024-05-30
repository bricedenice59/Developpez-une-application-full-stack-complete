package com.openclassrooms.mddapi.unit.security.services;

import com.openclassrooms.mddapi.models.Role;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.security.services.JwtService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtServiceTests {

    @InjectMocks
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        //node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"
        ReflectionTestUtils.setField(jwtService, "secretKey", "63aad59352bb166442ecd9d64e77210dc6308b003e3bad9c4194f304546cf68c");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationDuration", 8640000);
        var lstRoles= new ArrayList<Role>();
        var role = Role.builder()
                .name("USER")
                .createdAt(LocalDateTime.now())
                .build();
        lstRoles.add(role);

        String name = "yoga_firstname";
        String email = "yoga@studio.com";
        user = User.builder()
                .id(1)
                .email(email)
                .name(name)
                .roles(lstRoles)
                .build();
    }

    @Test
    @DisplayName("Test generate a JWT token")
    public void JwtUtilsTests_GenerateJWTToken_ShouldSuccessfullyProvideAJWTToken() {
        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getName());
        String token = jwtService.generateToken(claims, user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Test Validate a JWT token successfully")
    public void JwtUtilsTests_validateJwtToken_ShouldBeAValidAndNotExpiredToken() {
        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getName());
        String token = jwtService.generateToken(claims, user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Test Validate a JWT token that is already expired should not be a valid token")
    public void JwtUtilsTests_validateJwtToken_AlreadyExpired_ShouldNotBeAValidToken() {
        ReflectionTestUtils.setField(jwtService, "jwtExpirationDuration", -10000);

        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getName());
        String expiredToken = jwtService.generateToken(claims, user);

        boolean isValid = jwtService.isTokenValid(expiredToken, user);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Test extract user name (claim) from jwt token")
    public void JwtUtilsTests_getUserNameFromJwtToken_ShouldExtractSuccessfullyUserNameFromJwtToken() {
        var token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()  + 60*1000)) // expires in 1h
                .signWith(jwtService.getSignInKey())
                .compact();

        var username = jwtService.extractUserName(token);
        assertEquals(user.getEmail(), username);
    }
}
