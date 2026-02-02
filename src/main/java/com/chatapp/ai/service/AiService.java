package com.chatapp.ai.service;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    public boolean isToxic(String message) {
        if (message == null) return false;
        return message.toLowerCase().contains("hate")
            || message.toLowerCase().contains("abuse");
    }
}

