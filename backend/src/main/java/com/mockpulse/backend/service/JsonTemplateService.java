package com.mockpulse.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class JsonTemplateService {

    private final ObjectMapper objectMapper;

    public JsonTemplateService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String render(String template) {
        if (template == null || template.isBlank()) {
            return "{}";
        }
        try {
            JsonNode jsonNode = objectMapper.readTree(template);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception ignored) {
            return template;
        }
    }
}
