package com.chatapp.chat.service;

import com.chatapp.ai.service.ToxicityService;
import com.chatapp.chat.Message;
import com.chatapp.chat.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageService Tests")
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ToxicityService toxicityService;

    @InjectMocks
    private MessageService messageService;

    private Message testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new Message();
        testMessage.setId(1L);
        testMessage.setSenderId(1L);
        testMessage.setReceiverId(2L);
        testMessage.setContent("Hello, how are you?");
        testMessage.setDelivered(false);
        testMessage.setRead(false);
        testMessage.setToxic(false);
        testMessage.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should send message successfully")
    void testSendMessageSuccess() {
        // Arrange
        when(toxicityService.isToxic("Hello, how are you?")).thenReturn(false);
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // Act
        Message result = messageService.sendMessage(testMessage);

        // Assert
        assertNotNull(result);
        assertTrue(result.isDelivered());
        assertFalse(result.isToxic());

        verify(toxicityService, times(1)).isToxic("Hello, how are you?");
        verify(messageRepository, times(1)).save(testMessage);
    }

    @Test
    @DisplayName("Should detect toxic content and mark message")
    void testSendMessageDetectsToxicity() {
        // Arrange
        testMessage.setContent("I hate this");
        when(toxicityService.isToxic("I hate this")).thenReturn(true);
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // Act
        Message result = messageService.sendMessage(testMessage);

        // Assert
        assertNotNull(result);
        assertTrue(result.isToxic());

        verify(toxicityService, times(1)).isToxic("I hate this");
        verify(messageRepository, times(1)).save(testMessage);
    }

    @Test
    @DisplayName("Should set delivered flag to true on send")
    void testSendMessageSetsDeliveredFlag() {
        // Arrange
        testMessage.setDelivered(false);
        when(toxicityService.isToxic(anyString())).thenReturn(false);
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            assertTrue(msg.isDelivered());
            return msg;
        });

        // Act
        messageService.sendMessage(testMessage);

        // Assert
        verify(messageRepository, times(1)).save(argThat(msg -> msg.isDelivered()));
    }

    @Test
    @DisplayName("Should get messages by receiver ID")
    void testGetMessagesByReceiverSuccess() {
        // Arrange
        List<Message> messages = new ArrayList<>();
        messages.add(testMessage);
        when(messageRepository.findByReceiverId(2L)).thenReturn(messages);

        // Act
        List<Message> result = messageService.getMessagesByReceiver(2L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getReceiverId());

        verify(messageRepository, times(1)).findByReceiverId(2L);
    }

    @Test
    @DisplayName("Should return empty list when no messages for receiver")
    void testGetMessagesByReceiverNoMessages() {
        // Arrange
        when(messageRepository.findByReceiverId(anyLong())).thenReturn(new ArrayList<>());

        // Act
        List<Message> result = messageService.getMessagesByReceiver(999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(messageRepository, times(1)).findByReceiverId(999L);
    }

    @Test
    @DisplayName("Should mark message as read successfully")
    void testMarkReadSuccess() {
        // Arrange
        testMessage.setRead(false);
        when(messageRepository.findById(1L)).thenReturn(Optional.of(testMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // Act
        Message result = messageService.markRead(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isRead());

        verify(messageRepository, times(1)).findById(1L);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("Should throw exception when marking non-existent message as read")
    void testMarkReadMessageNotFound() {
        // Arrange
        when(messageRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> messageService.markRead(999L));
        assertEquals("Message not found", exception.getMessage());

        verify(messageRepository, times(1)).findById(999L);
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    @DisplayName("Should use ToxicityService for toxicity detection")
    void testSendMessageUsesToxicityService() {
        // Arrange
        when(toxicityService.isToxic("Hello, how are you?")).thenReturn(false);
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // Act
        messageService.sendMessage(testMessage);

        // Assert
        verify(toxicityService, times(1)).isToxic("Hello, how are you?");
    }

    @Test
    @DisplayName("Should save message to repository on send")
    void testSendMessageSavesToRepository() {
        // Arrange
        when(toxicityService.isToxic(anyString())).thenReturn(false);
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);

        // Act
        messageService.sendMessage(testMessage);

        // Assert
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("Should call repository findByReceiverId with correct ID")
    void testGetMessagesCallsRepositoryWithCorrectId() {
        // Arrange
        when(messageRepository.findByReceiverId(5L)).thenReturn(new ArrayList<>());

        // Act
        messageService.getMessagesByReceiver(5L);

        // Assert
        verify(messageRepository, times(1)).findByReceiverId(5L);
    }

    @Test
    @DisplayName("Should mark message read flag as true")
    void testMarkReadSetsFlagToTrue() {
        // Arrange
        when(messageRepository.findById(1L)).thenReturn(Optional.of(testMessage));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message msg = invocation.getArgument(0);
            assertTrue(msg.isRead());
            return msg;
        });

        // Act
        messageService.markRead(1L);

        // Assert
        verify(messageRepository, times(1)).save(argThat(Message::isRead));
    }

    @Test
    @DisplayName("Should return all messages for a receiver")
    void testGetMessagesByReceiverReturnsAllMessages() {
        // Arrange
        Message msg1 = new Message();
        msg1.setId(1L);
        Message msg2 = new Message();
        msg2.setId(2L);
        List<Message> messages = new ArrayList<>();
        messages.add(msg1);
        messages.add(msg2);

        when(messageRepository.findByReceiverId(2L)).thenReturn(messages);

        // Act
        List<Message> result = messageService.getMessagesByReceiver(2L);

        // Assert
        assertEquals(2, result.size());
    }
}

