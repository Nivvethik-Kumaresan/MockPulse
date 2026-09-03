package com.mockpulse.backend.repository;

import com.mockpulse.backend.model.MockRoute;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MockRouteRepository extends MongoRepository<MockRoute, String> {
    Optional<MockRoute> findByPathAndHttpMethod(String path, String httpMethod);
}
