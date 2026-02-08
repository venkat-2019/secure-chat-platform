package com.chatapp.user;

import com.chatapp.api.dto.ApiResponse;
import com.chatapp.api.dto.UserDTO;
import com.chatapp.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setStatus("ONLINE");
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should get user by ID and return 200 OK")
    void testGetUserSuccess() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.getUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User retrieved successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should return UserDTO without password")
    void testGetUserReturnsUserDTOWithoutPassword() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.getUser(1L);

        // Assert
        assertNotNull(response.getBody());
        UserDTO dto = response.getBody().getData();
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("ONLINE", dto.getStatus());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should update username and return 200 OK")
    void testUpdateUserSuccess() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newusername");
        updatedUser.setEmail("test@example.com");

        User requestUser = new User();
        requestUser.setUsername("newusername");

        when(userService.updateUsername(1L, "newusername")).thenReturn(updatedUser);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.updateUser(1L, requestUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Username updated successfully", response.getBody().getMessage());

        verify(userService, times(1)).updateUsername(1L, "newusername");
    }

    @Test
    @DisplayName("Should update status and return 200 OK")
    void testUpdateStatusSuccess() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setStatus("OFFLINE");

        when(userService.updateStatus(1L, "OFFLINE")).thenReturn(updatedUser);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.updateStatus(1L, "OFFLINE");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Status updated successfully", response.getBody().getMessage());

        verify(userService, times(1)).updateStatus(1L, "OFFLINE");
    }

    @Test
    @DisplayName("Should get user status and return 200 OK")
    void testGetStatusSuccess() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        ResponseEntity<ApiResponse<String>> response = userController.getStatus(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Status retrieved successfully", response.getBody().getMessage());
        assertEquals("ONLINE", response.getBody().getData());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should call UserService getUserById with correct ID")
    void testGetUserCallsServiceWithCorrectId() {
        // Arrange
        when(userService.getUserById(5L)).thenReturn(testUser);

        // Act
        userController.getUser(5L);

        // Assert
        verify(userService, times(1)).getUserById(5L);
    }

    @Test
    @DisplayName("Should call UserService updateUsername with correct parameters")
    void testUpdateUserCallsServiceWithCorrectParams() {
        // Arrange
        User requestUser = new User();
        requestUser.setUsername("updated");
        when(userService.updateUsername(1L, "updated")).thenReturn(testUser);

        // Act
        userController.updateUser(1L, requestUser);

        // Assert
        verify(userService, times(1)).updateUsername(1L, "updated");
    }

    @Test
    @DisplayName("Should call UserService updateStatus with correct parameters")
    void testUpdateStatusCallsServiceWithCorrectParams() {
        // Arrange
        when(userService.updateStatus(1L, "OFFLINE")).thenReturn(testUser);

        // Act
        userController.updateStatus(1L, "OFFLINE");

        // Assert
        verify(userService, times(1)).updateStatus(1L, "OFFLINE");
    }

    @Test
    @DisplayName("Should return correct response status codes for all operations")
    void testResponseStatusCodes() {
        // Arrange
        when(userService.getUserById(anyLong())).thenReturn(testUser);
        when(userService.updateStatus(anyLong(), anyString())).thenReturn(testUser);

        // Act & Assert
        assertEquals(HttpStatus.OK, userController.getUser(1L).getStatusCode());
        assertEquals(HttpStatus.OK, userController.updateStatus(1L, "ONLINE").getStatusCode());
        assertEquals(HttpStatus.OK, userController.getStatus(1L).getStatusCode());
    }

    @Test
    @DisplayName("Should return UserDTO with all required fields")
    void testUserDTOHasAllFields() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.getUser(1L);

        // Assert
        UserDTO dto = response.getBody().getData();
        assertNotNull(dto.getId());
        assertNotNull(dto.getUsername());
        assertNotNull(dto.getEmail());
        assertNotNull(dto.getStatus());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    @DisplayName("Should include success message in response")
    void testResponseIncludesSuccessMessage() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.getUser(1L);

        // Assert
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getMessage());
        assertFalse(response.getBody().getMessage().isEmpty());
    }
}

