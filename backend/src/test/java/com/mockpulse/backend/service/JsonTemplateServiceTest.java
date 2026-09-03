package com.mockpulse.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonTemplateServiceTest {

    private final JsonTemplateService jsonTemplateService = new JsonTemplateService(new ObjectMapper());

    @Test
    void shouldNormalizeValidJson() {
        String output = jsonTemplateService.render("{\n  \"message\": \"ok\"\n}");
        assertEquals("{\"message\":\"ok\"}", output);
    }

    @Test
    void shouldReturnRawValueWhenNotJson() {
        String output = jsonTemplateService.render("plain-text");
        assertEquals("plain-text", output);
    }
}
