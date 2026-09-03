package com.mockpulse.backend.service;

import com.mockpulse.backend.model.MockRoute;
import com.mockpulse.backend.repository.MockRouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MockRouteService {

    private final MockRouteRepository mockRouteRepository;

    public MockRouteService(MockRouteRepository mockRouteRepository) {
        this.mockRouteRepository = mockRouteRepository;
    }

    public List<MockRoute> findAll() {
        return mockRouteRepository.findAll();
    }

    public MockRoute save(MockRoute route) {
        return mockRouteRepository.save(route);
    }

    public Optional<MockRoute> findByPathAndMethod(String path, String method) {
        return mockRouteRepository.findByPathAndHttpMethod(path, method.toUpperCase());
    }
}
