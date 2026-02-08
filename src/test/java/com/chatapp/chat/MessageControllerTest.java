package com.chatapp.chat;

import com.chatapp.api.dto.ApiResponse;
import com.chatapp.api.dto.MessageDTO;
import com.chatapp.chat.service.MessageService;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageController Tests")
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    private Message testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new Message();
        testMessage.setId(1L);
        testMessage.setSenderId(1L);
        testMessage.setReceiverId(2L);
        testMessage.setContent("Hello");
        testMessage.setDelivered(true);
        testMessage.setRead(false);
        testMessage.setToxic(false);
        testMessage.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should send message and return 200 OK")
    void testSendMessageSuccess() {
        // Arrange
        when(messageService.sendMessage(any(Message.class))).thenReturn(testMessage);

        // Act
        ResponseEntity<ApiResponse<MessageDTO>> response = messageController.send(testMessage);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Message sent successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());

        verify(messageService, times(1)).sendMessage(any(Message.class));
    }

    @Test
    @DisplayName("Should return MessageDTO with correct fields")
    void testSendMessageReturnsCorrectDTO() {
        // Arrange
        when(messageService.sendMessage(any(Message.class))).thenReturn(testMessage);

        // Act
        ResponseEntity<ApiResponse<MessageDTO>> response = messageController.send(testMessage);

        // Assert
        MessageDTO dto = response.getBody().getData();
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getSenderId());
        assertEquals(2L, dto.getReceiverId());
        assertEquals("Hello", dto.getContent());
        assertTrue(dto.isDelivered());
        assertFalse(dto.isRead());
        assertFalse(dto.isToxic());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    @DisplayName("Should get messages by receiver and return 200 OK")
    void testGetMessagesByReceiverSuccess() {
        // Arrange
        List<Message> messages = new ArrayList<>();
        messages.add(testMessage);
        when(messageService.getMessagesByReceiver(2L)).thenReturn(messages);

        // Act
        ResponseEntity<ApiResponse<List<MessageDTO>>> response = messageController.get(2L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Messages retrieved successfully", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().size());

        verify(messageService, times(1)).getMessagesByReceiver(2L);
    }

    @Test
    @DisplayName("Should return empty list when no messages")
    void testGetMessagesByReceiverEmpty() {
        // Arrange
        when(messageService.getMessagesByReceiver(anyLong())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<ApiResponse<List<MessageDTO>>> response = messageController.get(999L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData().isEmpty());

        verify(messageService, times(1)).getMessagesByReceiver(999L);
    }

    @Test
    @DisplayName("Should mark message as read and return 200 OK")
    void testMarkReadSuccess() {
        // Arrange
        testMessage.setRead(true);
        when(messageService.markRead(1L)).thenReturn(testMessage);

        // Act
        ResponseEntity<ApiResponse<MessageDTO>> response = messageController.read(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Message marked as read", response.getBody().getMessage());
        assertTrue(response.getBody().getData().isRead());

        verify(messageService, times(1)).markRead(1L);
    }

    @Test
    @DisplayName("Should call MessageService sendMessage with correct message")
    void testSendMessageCallsService() {
        // Arrange
        when(messageService.sendMessage(any(Message.class))).thenReturn(testMessage);

        // Act
        messageController.send(testMessage);

        // Assert
        verify(messageService, times(1)).sendMessage(testMessage);
    }

    @Test
    @DisplayName("Should call MessageService getMessagesByReceiver with correct ID")
    void testGetMessagesCallsServiceWithCorrectId() {
        // Arrange
        when(messageService.getMessagesByReceiver(5L)).thenReturn(new ArrayList<>());

        // Act
        messageController.get(5L);

        // Assert
        verify(messageService, times(1)).getMessagesByReceiver(5L);
    }

    @Test
    @DisplayName("Should call MessageService markRead with correct ID")
    void testMarkReadCallsServiceWithCorrectId() {
        // Arrange
        when(messageService.markRead(10L)).thenReturn(testMessage);

        // Act
        messageController.read(10L);

        // Assert
        verify(messageService, times(1)).markRead(10L);
    }

    @Test
    @DisplayName("Should return correct HTTP status codes")
    void testResponseStatusCodes() {
        // Arrange
        when(messageService.sendMessage(any(Message.class))).thenReturn(testMessage);
        when(messageService.getMessagesByReceiver(anyLong())).thenReturn(new ArrayList<>());
        when(messageService.markRead(anyLong())).thenReturn(testMessage);

        // Act & Assert
        assertEquals(HttpStatus.OK, messageController.send(testMessage).getStatusCode());
        assertEquals(HttpStatus.OK, messageController.get(1L).getStatusCode());
        assertEquals(HttpStatus.OK, messageController.read(1L).getStatusCode());
    }

    @Test
    @DisplayName("Should convert Message to MessageDTO before response")
    void testMessageConvertedToDTO() {
        // Arrange
        when(messageService.sendMessage(any(Message.class))).thenReturn(testMessage);

        // Act
        ResponseEntity<ApiResponse<MessageDTO>> response = messageController.send(testMessage);

        // Assert
        MessageDTO dto = response.getBody().getData();
        assertEquals(testMessage.getId(), dto.getId());
        assertEquals(testMessage.getSenderId(), dto.getSenderId());
        assertEquals(testMessage.getReceiverId(), dto.getReceiverId());
        assertEquals(testMessage.getContent(), dto.getContent());
    }

    @Test
    @DisplayName("Should return multiple messages as MessageDTOs")
    void testMultipleMessagesConvertedToDTO() {
        // Arrange
        Message msg2 = new Message();
        msg2.setId(2L);
        msg2.setContent("Second message");

        List<Message> messages = new ArrayList<>();
        messages.add(testMessage);
        messages.add(msg2);

        when(messageService.getMessagesByReceiver(2L)).thenReturn(messages);

        // Act
        ResponseEntity<ApiResponse<List<MessageDTO>>> response = messageController.get(2L);

        // Assert
        assertEquals(2, response.getBody().getData().size());
        assertEquals(1L, response.getBody().getData().get(0).getId());
        assertEquals(2L, response.getBody().getData().get(1).getId());
    }

    @Test
    @DisplayName("Should include success flag in response")
    void testResponseIncludesSuccessFlag() {
        // Arrange
        when(messageService.sendMessage(any(Message.class))).thenReturn(testMessage);

        // Act
        ResponseEntity<ApiResponse<MessageDTO>> response = messageController.send(testMessage);

        // Assert
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    @DisplayName("Should include message in response")
    void testResponseIncludesMessage() {
        // Arrange
        when(messageService.sendMessage(any(Message.class))).thenReturn(testMessage);

        // Act
        ResponseEntity<ApiResponse<MessageDTO>> response = messageController.send(testMessage);

        // Assert
        assertNotNull(response.getBody().getMessage());
        assertFalse(response.getBody().getMessage().isEmpty());
    }
}

