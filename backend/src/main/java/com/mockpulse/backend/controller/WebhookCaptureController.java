package com.mockpulse.backend.controller;

import com.mockpulse.backend.service.WebhookBroadcastService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/webhooks")
public class WebhookCaptureController {

    private final WebhookBroadcastService webhookBroadcastService;

    public WebhookCaptureController(WebhookBroadcastService webhookBroadcastService) {
        this.webhookBroadcastService = webhookBroadcastService;
    }

    @RequestMapping(value = "/**", method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.PATCH,
            RequestMethod.DELETE
    })
    public ResponseEntity<Void> capture(@RequestBody(required = false) String payload, HttpServletRequest request) {
        String body = payload;
        if (body == null || body.isBlank()) {
            body = "{\"event\":\"" + request.getMethod() + " " + request.getRequestURI() + "\"}";
        }
        webhookBroadcastService.broadcast(body);
        return ResponseEntity.accepted().build();
    }
}
