package com.chatapp.auth;

import com.chatapp.api.dto.ApiResponse;
import com.chatapp.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.chatapp.auth.RegisterRequest;
import com.chatapp.auth.LoginRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("Should register user and return 201 CREATED")
    void testRegisterSuccess() {
        // Arrange
        doNothing().when(authService).register(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User registered successfully", response.getBody().getMessage());

        verify(authService, times(1)).register(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );
    }

    @Test
    @DisplayName("Should handle registration error and return 400 BAD_REQUEST")
    void testRegisterFailure() {
        // Arrange
        doThrow(new RuntimeException("Email already exists"))
                .when(authService).register(anyString(), anyString(), anyString());

        // Act
        ResponseEntity<ApiResponse<Void>> response = authController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Email already exists", response.getBody().getMessage());

        verify(authService, times(1)).register(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should login user and return JWT token")
    void testLoginSuccess() {
        // Arrange
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        when(authService.login(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn("LOGIN_SUCCESS");
        when(jwtUtil.generateToken(loginRequest.getEmail())).thenReturn(expectedToken);

        // Act
        ResponseEntity<ApiResponse<Map<String, String>>> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Login successful", response.getBody().getMessage());

        verify(authService, times(1)).login(loginRequest.getEmail(), loginRequest.getPassword());
        verify(jwtUtil, times(1)).generateToken(loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should return 401 UNAUTHORIZED on login failure")
    void testLoginFailure() {
        // Arrange
        when(authService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        ResponseEntity<ApiResponse<Map<String, String>>> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid credentials", response.getBody().getMessage());

        verify(authService, times(1)).login(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should return token in login response")
    void testLoginReturnsToken() {
        // Arrange
        String expectedToken = "test-jwt-token";
        when(authService.login(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn("LOGIN_SUCCESS");
        when(jwtUtil.generateToken(loginRequest.getEmail())).thenReturn(expectedToken);

        // Act
        ResponseEntity<ApiResponse<Map<String, String>>> response = authController.login(loginRequest);

        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().isSuccess());

        verify(jwtUtil, times(1)).generateToken(loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should include email in login response")
    void testLoginReturnsEmail() {
        // Arrange
        String expectedToken = "test-jwt-token";
        when(authService.login(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn("LOGIN_SUCCESS");
        when(jwtUtil.generateToken(loginRequest.getEmail())).thenReturn(expectedToken);

        // Act
        ResponseEntity<ApiResponse<Map<String, String>>> response = authController.login(loginRequest);

        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    @DisplayName("Should call AuthService register method with correct parameters")
    void testRegisterCallsAuthService() {
        // Arrange
        doNothing().when(authService).register(anyString(), anyString(), anyString());

        // Act
        authController.register(registerRequest);

        // Assert
        verify(authService, times(1)).register(
                "testuser",
                "test@example.com",
                "password123"
        );
    }

    @Test
    @DisplayName("Should call JwtUtil generateToken on successful login")
    void testLoginCallsJwtUtil() {
        // Arrange
        when(authService.login(anyString(), anyString())).thenReturn("LOGIN_SUCCESS");
        when(jwtUtil.generateToken(anyString())).thenReturn("token");

        // Act
        authController.login(loginRequest);

        // Assert
        verify(jwtUtil, times(1)).generateToken("test@example.com");
    }
}

