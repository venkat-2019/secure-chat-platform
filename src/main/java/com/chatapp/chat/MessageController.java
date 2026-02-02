package com.chatapp.chat;

import com.chatapp.chat.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    // SEND MESSAGE
    @PostMapping("/send")
    public ResponseEntity<Message> send(@RequestBody Message message) {
        return ResponseEntity.ok(service.sendMessage(message));
    }

    // GET OFFLINE MESSAGES
    @GetMapping("/receiver/{id}")
    public ResponseEntity<List<Message>> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMessagesByReceiver(id));
    }

    // MARK MESSAGE AS READ
    @PutMapping("/read/{id}")
    public ResponseEntity<String> read(@PathVariable Long id) {
        service.markRead(id);
        return ResponseEntity.ok("Read");
    }
}
