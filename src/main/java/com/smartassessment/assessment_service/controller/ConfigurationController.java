package com.smartassessment.assessment_service.controller;

import com.smartassessment.assessment_service.dto.request.ConfigurationRequest;
import com.smartassessment.assessment_service.dto.response.ExamConfigResponse;
import com.smartassessment.assessment_service.service.ConfigurationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configurations")
@RequiredArgsConstructor
@Slf4j
public class ConfigurationController {

    private final ConfigurationService configurationService;

    // POST /api/configurations
    @PostMapping
    public ResponseEntity<ExamConfigResponse> saveConfiguration(@Valid @RequestBody ConfigurationRequest request) {
        log.info("Request received: Save Configuration for assessmentId={}", request.getAssessmentId());
        return ResponseEntity.ok(configurationService.saveConfiguration(request));
    }

    // GET /api/configurations/{assessmentId}
    @GetMapping("/{assessmentId}")
    public ResponseEntity<ExamConfigResponse> getConfiguration(@PathVariable Long assessmentId) {
        log.info("Request received: Get Configuration for assessmentId={}", assessmentId);
        return ResponseEntity.ok(configurationService.getConfiguration(assessmentId));
    }
}
