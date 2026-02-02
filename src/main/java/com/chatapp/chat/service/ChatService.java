package com.chatapp.chat.service;

import com.chatapp.ai.service.AiService;
import com.chatapp.chat.Message;
import com.chatapp.chat.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final MessageRepository repo;
    private final AiService aiService;

    public ChatService(MessageRepository repo, AiService aiService) {
        this.repo = repo;
        this.aiService = aiService;
    }

    public Message handleMessage(Message message) {
        message.setToxic(aiService.isToxic(message.getContent()));
        message.setDelivered(true);
        message.setRead(false);
        return repo.save(message);
    }
}
