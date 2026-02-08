package com.chatapp.chat.service;

import com.chatapp.ai.service.ToxicityService;
import com.chatapp.chat.Message;
import com.chatapp.chat.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repo;
    private final ToxicityService toxicityService;

    public MessageService(MessageRepository repo, ToxicityService toxicityService) {
        this.repo = repo;
        this.toxicityService = toxicityService;
    }

    public Message sendMessage(Message message) {
        message.setDelivered(true);
        message.setToxic(toxicityService.isToxic(message.getContent()));
        return repo.save(message);
    }

    public List<Message> getMessagesByReceiver(Long receiverId) {
        return repo.findByReceiverId(receiverId);
    }

    public Message markRead(Long id) {
        Message msg = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        msg.setRead(true);
        return repo.save(msg);
    }
}
