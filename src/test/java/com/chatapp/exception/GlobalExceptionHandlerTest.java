package com.chatapp.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should handle RuntimeException with BAD_REQUEST status")
    void testHandleRuntimeException() {
        // Arrange
        RuntimeException exception = new RuntimeException("Test error message");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Test error message", response.getBody().get("message"));
        assertEquals("Bad Request", response.getBody().get("error"));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with BAD_REQUEST status")
    void testHandleIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Invalid argument", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Should handle generic Exception with INTERNAL_SERVER_ERROR status")
    void testHandleGenericException() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Internal Server Error", response.getBody().get("error"));
    }

    @Test
    @DisplayName("Should include timestamp in error response")
    void testErrorResponseIncludesTimestamp() {
        // Arrange
        RuntimeException exception = new RuntimeException("Error");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        assertTrue(response.getBody().containsKey("timestamp"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    @DisplayName("Should include error message in response")
    void testErrorResponseIncludesMessage() {
        // Arrange
        String errorMessage = "Email already exists";
        RuntimeException exception = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    @DisplayName("Should include status code in response")
    void testErrorResponseIncludesStatusCode() {
        // Arrange
        RuntimeException exception = new RuntimeException("Error");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    @DisplayName("Should include error type in response")
    void testErrorResponseIncludesErrorType() {
        // Arrange
        RuntimeException exception = new RuntimeException("Error");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        assertEquals("Bad Request", response.getBody().get("error"));
    }

    @Test
    @DisplayName("Should use correct HTTP status for RuntimeException")
    void testRuntimeExceptionStatusCode() {
        // Arrange
        RuntimeException exception = new RuntimeException("Test");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Should use correct HTTP status for IllegalArgumentException")
    void testIllegalArgumentExceptionStatusCode() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Test");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleIllegalArgumentException(exception);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Should use correct HTTP status for generic Exception")
    void testGenericExceptionStatusCode() {
        // Arrange
        Exception exception = new Exception("Test");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Should have all required response fields")
    void testResponseHasAllRequiredFields() {
        // Arrange
        RuntimeException exception = new RuntimeException("Error message");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        Map<String, Object> body = response.getBody();
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("status"));
        assertTrue(body.containsKey("error"));
        assertTrue(body.containsKey("message"));
    }

    @Test
    @DisplayName("Should handle different exception messages")
    void testHandlesDifferentMessages() {
        // Arrange
        String[] messages = {
                "User not found",
                "Email already exists",
                "Invalid credentials",
                "Database error"
        };

        // Act & Assert
        for (String msg : messages) {
            RuntimeException exception = new RuntimeException(msg);
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);
            assertEquals(msg, response.getBody().get("message"));
        }
    }

    @Test
    @DisplayName("Should not expose sensitive information in error messages")
    void testDoesNotExposeSensitiveInfo() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

        // Assert
        Map<String, Object> body = response.getBody();
        assertEquals("An unexpected error occurred", body.get("message"));
    }

    @Test
    @DisplayName("Should handle exception with empty message")
    void testHandlesEmptyExceptionMessage() {
        // Arrange
        RuntimeException exception = new RuntimeException("");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        assertEquals("", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Should handle exception with null message")
    void testHandlesNullExceptionMessage() {
        // Arrange
        RuntimeException exception = new RuntimeException((String) null);

        // Act & Assert
        assertDoesNotThrow(() -> exceptionHandler.handleRuntimeException(exception));
    }

    @Test
    @DisplayName("Should return ResponseEntity with proper status code object")
    void testReturnsProperResponseEntity() {
        // Arrange
        RuntimeException exception = new RuntimeException("Error");

        // Act
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleRuntimeException(exception);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

