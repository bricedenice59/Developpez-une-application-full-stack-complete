package com.openclassrooms.mddapi.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.mddapi.exceptions.UserAlreadyExistException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payloads.requests.UpdateUserDetailsRequest;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Test retrieve a user with id should be successful")
    public void UserServiceTests_getById_UserExists_ShouldReturnUser() {
        Integer userId = 1;
        User mockUser = new User();
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    @DisplayName("Test retrieve a user with a non existent id should fail and throw a UserNotFoundException")
    public void UserServiceTests_getById_UserDoesNotExist_ShouldThrowAUserNotFoundException() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
    }

    @Test
    @DisplayName("Test retrieve a user with email should be successful")
    public void UserServiceTests_getByEmail_EmailExists_ShouldReturnUser() {
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        User result = userService.getByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Test retrieve a user with a non existent email should fail and throw a UserNotFoundException")
    public void UserServiceTests_getByEmail_EmailDoesNotExist_ShouldThrowAUserNotFoundException() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getByEmail(email));
    }

    @Test
    @DisplayName("Test update a user should be successful")
    public void UserServiceTests_updateFromRequest_ShouldUpdateUserSuccessfully() {
        var userId = 1;
        String newEmail = "newemail@example.com";
        String newName = "New Name";
        var detailsRequest = UpdateUserDetailsRequest.builder()
                .email(newEmail)
                .name(newName).build();

        User mockUser = new User();
        mockUser.setId(userId);

        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());

        userService.updateFromRequest(mockUser, true, detailsRequest);

        verify(userRepository).save(mockUser);
        assertEquals(newEmail, mockUser.getEmail());
        assertEquals(newName, mockUser.getName());
    }

    @Test
    @DisplayName("Test retrieve a post with a non existent id should fail and throw a PostNotFoundException")
    public void UserServiceTests_updateFromRequest_WithEmailMatchingAnotherUser_ShouldThrowAUserAlreadyExistException() {
        String newName = "New Name";
        String existingEmail = "existingemail@example.com";
        var detailsRequest = UpdateUserDetailsRequest.builder()
                .email(existingEmail)
                .name(newName).build();

        User existingUser = new User();
        existingUser.setEmail(existingEmail);
        when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistException.class, () -> userService.updateFromRequest(existingUser, true, detailsRequest));
    }
}
