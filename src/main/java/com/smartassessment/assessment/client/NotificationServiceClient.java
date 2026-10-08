package com.smartassessment.assessment.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
public class NotificationServiceClient {

    private final RestTemplate restTemplate;
    private final String notificationServiceUrl;

    public NotificationServiceClient(
            RestTemplateBuilder builder,
            @Value("${app.services.notification-service.url:http://localhost:8087}") String notificationServiceUrl) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        this.notificationServiceUrl = notificationServiceUrl;
    }

    @Async
    public void sendCandidateInvitation(
            String candidateEmail, String candidateName, String assessmentTitle, String token, String passcode) {
        try {
            String url = notificationServiceUrl + "/api/notifications/assessment-invite";
            Map<String, Object> payload = Map.of(
                    "recipientEmail", candidateEmail,
                    "recipientName", candidateName,
                    "assessmentTitle", assessmentTitle,
                    "invitationToken", token,
                    "passcode", passcode != null ? passcode : ""
            );
            log.info("Sending candidate invitation async to {} for assessment '{}'", candidateEmail, assessmentTitle);
            restTemplate.postForLocation(url, payload);
        } catch (RestClientException ex) {
            log.warn("Failed to send async notification to {}: {}", candidateEmail, ex.getMessage());
        }
    }
}
