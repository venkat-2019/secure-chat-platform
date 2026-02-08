package com.chatapp.user.service;

import com.chatapp.user.User;
import com.chatapp.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setStatus("ONLINE");
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void testGetUserByIdSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testGetUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(999L));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update username successfully")
    void testUpdateUsernameSuccess() {
        // Arrange
        String newUsername = "newusername";
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername(newUsername);
        updatedUser.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUsername(1L, newUsername);

        // Assert
        assertNotNull(result);
        assertEquals(newUsername, result.getUsername());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when updating username for non-existent user")
    void testUpdateUsernameUserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUsername(999L, "newname"));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should update status successfully")
    void testUpdateStatusSuccess() {
        // Arrange
        String newStatus = "OFFLINE";
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("testuser");
        updatedUser.setStatus(newStatus);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateStatus(1L, newStatus);

        // Assert
        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when updating status for non-existent user")
    void testUpdateStatusUserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateStatus(999L, "OFFLINE"));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should save user with updated username")
    void testUpdateUsernameSavesChanges() {
        // Arrange
        String newUsername = "updateduser";
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(newUsername, user.getUsername());
            return user;
        });

        // Act
        userService.updateUsername(1L, newUsername);

        // Assert
        verify(userRepository, times(1)).save(argThat(user -> newUsername.equals(user.getUsername())));
    }

    @Test
    @DisplayName("Should save user with updated status")
    void testUpdateStatusSavesChanges() {
        // Arrange
        String newStatus = "OFFLINE";
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(newStatus, user.getStatus());
            return user;
        });

        // Act
        userService.updateStatus(1L, newStatus);

        // Assert
        verify(userRepository, times(1)).save(argThat(user -> newStatus.equals(user.getStatus())));
    }

    @Test
    @DisplayName("Should call repository findById with correct ID")
    void testGetUserCallsRepositoryWithCorrectId() {
        // Arrange
        when(userRepository.findById(5L)).thenReturn(Optional.of(testUser));

        // Act
        userService.getUserById(5L);

        // Assert
        verify(userRepository, times(1)).findById(5L);
    }

    @Test
    @DisplayName("Should call repository save on update")
    void testUpdateCallsRepositorySave() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.updateUsername(1L, "newname");

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }
}

