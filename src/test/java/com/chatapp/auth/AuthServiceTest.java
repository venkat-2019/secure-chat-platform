package com.chatapp.auth;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setStatus("OFFLINE");
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void testRegisterUserSuccess() {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        assertDoesNotThrow(() -> authService.register(username, email, password));

        // Assert
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegisterUserWithExistingEmail() {
        // Arrange
        String username = "newuser";
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(username, email, password));
        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should login user successfully")
    void testLoginUserSuccess() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        String result = authService.login(email, password);

        // Assert
        assertEquals("LOGIN_SUCCESS", result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testLoginUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(email, password));
        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void testLoginUserWithWrongPassword() {
        // Arrange
        String email = "test@example.com";
        String wrongPassword = "wrongpassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(email, wrongPassword));
        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should update user status to ONLINE on successful login")
    void testLoginUpdateUserStatus() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        User onlineUser = new User();
        onlineUser.setId(1L);
        onlineUser.setEmail(email);
        onlineUser.setPassword(password);
        onlineUser.setStatus("OFFLINE");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(onlineUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals("ONLINE", user.getStatus());
            return user;
        });

        // Act
        authService.login(email, password);

        // Assert
        verify(userRepository, times(1)).save(argThat(user -> "ONLINE".equals(user.getStatus())));
    }

    @Test
    @DisplayName("Should save user with correct details on registration")
    void testRegisterSavesUserWithCorrectDetails() {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(username, user.getUsername());
            assertEquals(email, user.getEmail());
            assertEquals(password, user.getPassword());
            assertEquals("OFFLINE", user.getStatus());
            return user;
        });

        // Act
        authService.register(username, email, password);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }
}

