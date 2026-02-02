package com.chatapp.ai.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ToxicityService {

    private static final List<String> BAD_WORDS =
            List.of("hate", "kill", "stupid", "idiot");

    public boolean isToxic(String message) {
        return BAD_WORDS.stream()
                .anyMatch(word -> message.toLowerCase().contains(word));
    }
}
