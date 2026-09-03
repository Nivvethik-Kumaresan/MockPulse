package com.mockpulse.backend.controller;

import com.mockpulse.backend.model.MockRoute;
import com.mockpulse.backend.service.JsonTemplateService;
import com.mockpulse.backend.service.LatencySimulationService;
import com.mockpulse.backend.service.MockRouteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class DynamicDispatcherController {

    private final MockRouteService mockRouteService;
    private final LatencySimulationService latencySimulationService;
    private final JsonTemplateService jsonTemplateService;

    public DynamicDispatcherController(
            MockRouteService mockRouteService,
            LatencySimulationService latencySimulationService,
            JsonTemplateService jsonTemplateService
    ) {
        this.mockRouteService = mockRouteService;
        this.latencySimulationService = latencySimulationService;
        this.jsonTemplateService = jsonTemplateService;
    }

    @RequestMapping(value = "/**", method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.PATCH,
            RequestMethod.DELETE,
            RequestMethod.OPTIONS,
            RequestMethod.HEAD
    })
    public ResponseEntity<String> dispatch(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        Optional<MockRoute> route = mockRouteService.findByPathAndMethod(path, method);
        if (route.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MockRoute mockRoute = route.get();
        latencySimulationService.applyDelay(mockRoute.getDelayMs());

        HttpHeaders headers = new HttpHeaders();
        Map<String, String> responseHeaders = mockRoute.getResponseHeaders();
        if (responseHeaders != null) {
            responseHeaders.forEach(headers::add);
        }
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        String body = jsonTemplateService.render(mockRoute.getResponseBody());
        return new ResponseEntity<>(body, headers, HttpStatusCode.valueOf(mockRoute.getResponseStatus()));
    }
}
