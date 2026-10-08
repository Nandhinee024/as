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
public class QuestionBankClient {

    private final RestTemplate restTemplate;
    private final String questionBankServiceUrl;

    public QuestionBankClient(
            RestTemplateBuilder builder,
            @Value("${app.services.question-bank-service.url:http://localhost:8083}") String questionBankServiceUrl) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        this.questionBankServiceUrl = questionBankServiceUrl;
    }

    public boolean validateQuestionSetExists(String questionSetId, int requiredCount) {
        if (questionSetId == null || questionSetId.isBlank()) {
            return true; // Optional set linkage
        }

        try {
            String url = questionBankServiceUrl + "/api/questions/sets/" + questionSetId + "/validate?minCount=" + requiredCount;
            log.info("Validating question set {} via Question Bank Service: {}", questionSetId, url);
            
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object validObj = response.getBody().get("valid");
                return validObj instanceof Boolean && (Boolean) validObj;
            }
            return true;
        } catch (RestClientException ex) {
            log.warn("Question Bank Service not reachable or returned error: {}. Proceeding with graceful fallback.", ex.getMessage());
            // Graceful fallback according to section 48/49 of FRS
            return true;
        }
    }
}
