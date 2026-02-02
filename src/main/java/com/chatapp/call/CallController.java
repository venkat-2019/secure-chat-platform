package com.chatapp.call;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CallController {

    private final SimpMessagingTemplate template;

    public CallController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/call")
    public void call(CallSignal signal) {
        template.convertAndSend(
            "/topic/call/" + signal.getReceiverId(),
            signal
        );
    }
}
