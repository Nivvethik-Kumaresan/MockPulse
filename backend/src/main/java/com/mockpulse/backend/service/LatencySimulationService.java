package com.mockpulse.backend.service;

import org.springframework.stereotype.Service;

@Service
public class LatencySimulationService {

    public void applyDelay(int delayMs) {
        if (delayMs <= 0) {
            return;
        }
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
