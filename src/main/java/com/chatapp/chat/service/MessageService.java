package com.chatapp.chat.service;

import com.chatapp.chat.Message;
import com.chatapp.chat.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    // AI Toxicity Detection (basic – assignment acceptable)
    private boolean isToxic(String text) {
        String t = text.toLowerCase();
        return t.contains("hate") || t.contains("kill") || t.contains("abuse");
    }

    public Message sendMessage(Message message) {
        message.setDelivered(true);
        message.setToxic(isToxic(message.getContent()));
        return repo.save(message);
    }

    public List<Message> getMessagesByReceiver(Long receiverId) {
        return repo.findByReceiverId(receiverId);
    }

    public Message markRead(Long id) {
        Message msg = repo.findById(id).orElseThrow();
        msg.setRead(true);
        return repo.save(msg);
    }
}
