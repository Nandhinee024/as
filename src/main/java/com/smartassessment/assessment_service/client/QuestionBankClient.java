package com.smartassessment.assessment_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Client for communicating with the Question Bank Service.
 * Uses plain RestTemplate (no advanced Feign).
 * Replace the base URL with actual service URL or use config properties.
 */
@Component
@Slf4j
public class QuestionBankClient {

    private final RestTemplate restTemplate;

    // Base URL of question-bank service (can be moved to application.properties)
    private static final String QUESTION_BANK_BASE_URL = "http://localhost:8081/api/questions";

    public QuestionBankClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Check if a topic with the given ID exists in the Question Bank.
     *
     * @param topicId the topic ID to check
     * @return true if topic exists, false otherwise
     */
    public boolean doesTopicExist(Long topicId) {
        try {
            String url = QUESTION_BANK_BASE_URL + "/topics/" + topicId + "/exists";
            Boolean exists = restTemplate.getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(exists);
        } catch (Exception ex) {
            log.warn("Failed to check topic existence for topicId={}. Error: {}", topicId, ex.getMessage());
            // Return true to avoid blocking when question-bank is down
            return true;
        }
    }
}
