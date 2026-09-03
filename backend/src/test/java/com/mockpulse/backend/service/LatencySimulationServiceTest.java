package com.mockpulse.backend.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LatencySimulationServiceTest {

    private final LatencySimulationService latencySimulationService = new LatencySimulationService();

    @Test
    void shouldApplyConfiguredDelay() {
        long start = System.currentTimeMillis();
        latencySimulationService.applyDelay(25);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed >= 20, "Expected at least ~20ms delay, got " + elapsed + "ms");
    }
}
