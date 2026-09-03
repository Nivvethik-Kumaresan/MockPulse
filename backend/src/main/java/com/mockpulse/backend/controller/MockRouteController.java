package com.mockpulse.backend.controller;

import com.mockpulse.backend.model.MockRoute;
import com.mockpulse.backend.service.MockRouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/mock-routes")
public class MockRouteController {

    private final MockRouteService mockRouteService;

    public MockRouteController(MockRouteService mockRouteService) {
        this.mockRouteService = mockRouteService;
    }

    @GetMapping
    public List<MockRoute> getRoutes() {
        return mockRouteService.findAll();
    }

    @PostMapping
    public MockRoute createRoute(@RequestBody MockRoute route) {
        if (route.getHttpMethod() != null) {
            route.setHttpMethod(route.getHttpMethod().toUpperCase());
        }
        return mockRouteService.save(route);
    }
}
