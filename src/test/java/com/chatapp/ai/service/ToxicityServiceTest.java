package com.chatapp.ai.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ToxicityService Tests")
class ToxicityServiceTest {

    @InjectMocks
    private ToxicityService toxicityService;

    @Test
    @DisplayName("Should detect 'hate' keyword as toxic")
    void testDetectHateKeyword() {
        // Act
        boolean result = toxicityService.isToxic("I hate this");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should detect 'kill' keyword as toxic")
    void testDetectKillKeyword() {
        // Act
        boolean result = toxicityService.isToxic("I want to kill you");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should detect 'stupid' keyword as toxic")
    void testDetectStupidKeyword() {
        // Act
        boolean result = toxicityService.isToxic("That's stupid");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should detect 'idiot' keyword as toxic")
    void testDetectIdiotKeyword() {
        // Act
        boolean result = toxicityService.isToxic("You are an idiot");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for non-toxic content")
    void testNonToxicContent() {
        // Act
        boolean result = toxicityService.isToxic("Hello, how are you?");

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should be case-insensitive in detection")
    void testCaseInsensitiveDetection() {
        // Act & Assert
        assertTrue(toxicityService.isToxic("HATE"));
        assertTrue(toxicityService.isToxic("Hate"));
        assertTrue(toxicityService.isToxic("KILL"));
        assertTrue(toxicityService.isToxic("Kill"));
    }

    @Test
    @DisplayName("Should detect toxic word in middle of sentence")
    void testDetectToxicWordInMiddle() {
        // Act
        boolean result = toxicityService.isToxic("This is really stupid behavior");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should detect toxic word at start of sentence")
    void testDetectToxicWordAtStart() {
        // Act
        boolean result = toxicityService.isToxic("Stupid person");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should detect toxic word at end of sentence")
    void testDetectToxicWordAtEnd() {
        // Act
        boolean result = toxicityService.isToxic("You are so stupid");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for empty message")
    void testEmptyMessage() {
        // Act
        boolean result = toxicityService.isToxic("");

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false for null message")
    void testNullMessage() {
        // Act
        boolean result = toxicityService.isToxic(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should detect multiple toxic words in one message")
    void testMultipleToxicWords() {
        // Act
        boolean result = toxicityService.isToxic("I hate stupid people");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for similar but non-toxic words")
    void testSimilarNonToxicWords() {
        // Act & Assert
        assertFalse(toxicityService.isToxic("We have a plan"));
        assertFalse(toxicityService.isToxic("Help is available"));
        assertFalse(toxicityService.isToxic("She is smart"));
    }

    @Test
    @DisplayName("Should detect toxic word with punctuation")
    void testToxicWordWithPunctuation() {
        // Act
        boolean result = toxicityService.isToxic("I hate! You stupid!");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return true when hate keyword is present")
    void testHateKeywordDetection() {
        // Act
        boolean result = toxicityService.isToxic("This food is really bad, I hate it");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false for clean message with various content")
    void testCleanMessages() {
        // Act & Assert
        assertFalse(toxicityService.isToxic("Good morning! How's your day?"));
        assertFalse(toxicityService.isToxic("Thanks for your help!"));
        assertFalse(toxicityService.isToxic("I love this project"));
    }
}

