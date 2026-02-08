package com.chatapp.security.jwt;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUtil Tests")
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // Set values using reflection for testing
        ReflectionTestUtils.setField(jwtUtil, "secret", "SecureChatPlatformSecretKeyForJWTGeneration123456789");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24 hours
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateTokenSuccess() {
        // Act
        String token = jwtUtil.generateToken("testuser@example.com");

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Should generate token with correct format (3 parts)")
    void testGenerateTokenFormat() {
        // Act
        String token = jwtUtil.generateToken("user@example.com");

        // Assert
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void testExtractUsernameSuccess() {
        // Arrange
        String username = "testuser@example.com";
        String token = jwtUtil.generateToken(username);

        // Act
        String extracted = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(username, extracted);
    }

    @Test
    @DisplayName("Should validate a valid token")
    void testValidateTokenSuccess() {
        // Arrange
        String token = jwtUtil.generateToken("user@example.com");

        // Act
        boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false for invalid token")
    void testValidateInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for corrupted token")
    void testValidateCorruptedToken() {
        // Arrange
        String token = jwtUtil.generateToken("user@example.com");
        String corruptedToken = token.substring(0, token.length() - 5) + "xxxxx";

        // Act
        boolean isValid = jwtUtil.validateToken(corruptedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should check token expiration")
    void testIsTokenExpired() {
        // Arrange
        String token = jwtUtil.generateToken("user@example.com");

        // Act
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Assert
        assertFalse(isExpired); // Just created token shouldn't be expired
    }

    @Test
    @DisplayName("Should generate valid tokens with username claim")
    void testGenerateTokenVariation() {
        // Act
        String token1 = jwtUtil.generateToken("user@example.com");
        String token2 = jwtUtil.generateToken("user@example.com");

        // Assert - verify both tokens are valid and contain the username
        assertNotNull(token1);
        assertNotNull(token2);
        assertTrue(token1.contains("."));
        assertTrue(token2.contains("."));
        assertEquals("user@example.com", jwtUtil.extractUsername(token1));
        assertEquals("user@example.com", jwtUtil.extractUsername(token2));
        assertTrue(jwtUtil.validateToken(token1));
        assertTrue(jwtUtil.validateToken(token2));
    }

    @Test
    @DisplayName("Should extract correct username from multiple tokens")
    void testExtractUsernameMultipleTokens() {
        // Arrange
        String user1 = "user1@example.com";
        String user2 = "user2@example.com";

        String token1 = jwtUtil.generateToken(user1);
        String token2 = jwtUtil.generateToken(user2);

        // Act
        String extracted1 = jwtUtil.extractUsername(token1);
        String extracted2 = jwtUtil.extractUsername(token2);

        // Assert
        assertEquals(user1, extracted1);
        assertEquals(user2, extracted2);
        assertNotEquals(extracted1, extracted2);
    }

    @Test
    @DisplayName("Should validate all valid tokens")
    void testValidateMultipleValidTokens() {
        // Arrange
        String token1 = jwtUtil.generateToken("user1@example.com");
        String token2 = jwtUtil.generateToken("user2@example.com");

        // Act
        boolean isValid1 = jwtUtil.validateToken(token1);
        boolean isValid2 = jwtUtil.validateToken(token2);

        // Assert
        assertTrue(isValid1);
        assertTrue(isValid2);
    }

    @Test
    @DisplayName("Should handle empty string username")
    void testGenerateTokenEmptyUsername() {
        // Act & Assert
        assertDoesNotThrow(() -> jwtUtil.generateToken(""));
    }

    @Test
    @DisplayName("Should handle token with special characters in username")
    void testTokenWithSpecialCharacters() {
        // Arrange
        String username = "user+special@example.com";

        // Act
        String token = jwtUtil.generateToken(username);
        String extracted = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(username, extracted);
    }

    @Test
    @DisplayName("Should return valid JWT structure")
    void testTokenStructure() {
        // Act
        String token = jwtUtil.generateToken("user@example.com");

        // Assert
        assertTrue(token.matches("[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+"));
    }

    @Test
    @DisplayName("Should generate token for valid email format")
    void testTokenGenerationForValidEmail() {
        // Arrange
        String email = "valid.email+tag@example.com";

        // Act
        String token = jwtUtil.generateToken(email);

        // Assert
        assertNotNull(token);
        assertEquals(email, jwtUtil.extractUsername(token));
    }

    @Test
    @DisplayName("Should throw exception for null token validation")
    void testValidateNullToken() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.validateToken(null));
    }

    @Test
    @DisplayName("Should handle token with various valid characters")
    void testTokenWithVariousCharacters() {
        // Arrange
        String username = "user.name_123@test.co.uk";

        // Act
        String token = jwtUtil.generateToken(username);
        String extracted = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(username, extracted);
    }
}

