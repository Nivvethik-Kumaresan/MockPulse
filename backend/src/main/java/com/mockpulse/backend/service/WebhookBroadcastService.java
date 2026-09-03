package com.mockpulse.backend.service;

import com.mockpulse.backend.websocket.WebhookWebSocketHandler;
import org.springframework.stereotype.Service;

@Service
public class WebhookBroadcastService {

    private final WebhookWebSocketHandler webSocketHandler;

    public WebhookBroadcastService(WebhookWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    public void broadcast(String payload) {
        webSocketHandler.broadcast(payload);
    }
}
