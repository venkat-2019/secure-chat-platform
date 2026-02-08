package com.chatapp.chat;

import com.chatapp.api.dto.ApiResponse;
import com.chatapp.api.dto.MessageDTO;
import com.chatapp.chat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    // SEND MESSAGE
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<MessageDTO>> send(@RequestBody Message message) {
        Message savedMessage = service.sendMessage(message);
        MessageDTO dto = convertToDTO(savedMessage);
        return ResponseEntity.ok(new ApiResponse<>(true, "Message sent successfully", dto));
    }

    // GET OFFLINE MESSAGES
    @GetMapping("/receiver/{id}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> get(@PathVariable Long id) {
        List<Message> messages = service.getMessagesByReceiver(id);
        List<MessageDTO> dtos = messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Messages retrieved successfully", dtos));
    }

    // MARK MESSAGE AS READ
    @PutMapping("/read/{id}")
    public ResponseEntity<ApiResponse<MessageDTO>> read(@PathVariable Long id) {
        Message message = service.markRead(id);
        MessageDTO dto = convertToDTO(message);
        return ResponseEntity.ok(new ApiResponse<>(true, "Message marked as read", dto));
    }

    private MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent(),
                message.isDelivered(),
                message.isRead(),
                message.isToxic(),
                message.getCreatedAt()
        );
    }
}
