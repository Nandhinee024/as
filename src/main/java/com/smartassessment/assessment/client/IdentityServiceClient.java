package com.smartassessment.assessment.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
public class IdentityServiceClient {

    private final RestTemplate restTemplate;
    private final String identityServiceUrl;

    public IdentityServiceClient(
            RestTemplateBuilder builder,
            @Value("${app.services.identity-service.url:http://localhost:8081}") String identityServiceUrl) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        this.identityServiceUrl = identityServiceUrl;
    }

    public boolean validateCandidateActive(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) {
            return false;
        }

        try {
            String url = identityServiceUrl + "/api/users/" + candidateId;
            log.info("Validating candidate {} with Identity Service: {}", candidateId, url);
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException ex) {
            log.warn("Identity Service unavailable for candidate validation (id={}): {}. Falling back gracefully.", candidateId, ex.getMessage());
            return true;
        }
    }
}
