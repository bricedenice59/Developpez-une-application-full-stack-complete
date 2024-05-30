package com.openclassrooms.mddapi.unit.security.jwt;

import com.openclassrooms.mddapi.models.Role;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.security.UserDetailsServiceImpl;
import com.openclassrooms.mddapi.security.services.JwtFilterService;
import com.openclassrooms.mddapi.security.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTests {

    @InjectMocks
    private JwtFilterService jwtFilterService;

    @Test
    @DisplayName("Test doFilterInternal with no token provided(no authentication needed)")
    public void AuthTokenFilterTests_doFilterInternal_WithNoAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        jwtFilterService.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Test doFilterInternal with a token provided")
    public void AuthTokenFilterTests_doFilterInternal_WithValidJwtAuthentication() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        JwtService jwtService = mock(JwtService.class);
        UserDetailsServiceImpl userDetailsService = mock(UserDetailsServiceImpl.class);

        String jwt = "valid.jwt.token";
        String username = "user@user.com";
        String headerName = "Authorization";
        String headerValue = "Bearer ";

        var lstRoles= new ArrayList<Role>();
        var role = Role.builder()
                .name("USER")
                .createdAt(LocalDateTime.now())
                .build();
        lstRoles.add(role);
        var user = User.builder()
                .email(username)
                .roles(lstRoles)
                .build();

        when(request.getHeader(headerName)).thenReturn(headerValue+ jwt);
        when(jwtService.isTokenValid(jwt, user)).thenReturn(true);
        when(jwtService.extractUserName(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);

        ReflectionTestUtils.setField(jwtFilterService, "jwtService", jwtService);
        ReflectionTestUtils.setField(jwtFilterService, "userDetailsService", userDetailsService);

        jwtFilterService.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}