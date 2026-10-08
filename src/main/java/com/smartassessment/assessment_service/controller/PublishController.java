package com.smartassessment.assessment_service.controller;

import com.smartassessment.assessment_service.dto.response.AssessmentResponse;
import com.smartassessment.assessment_service.service.PublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
@Slf4j
public class PublishController {

    private final PublishService publishService;

    // PUT /api/assessments/{id}/publish
    @PutMapping("/{id}/publish")
    public ResponseEntity<AssessmentResponse> publishAssessment(@PathVariable Long id) {
        log.info("Request received: Publish Assessment - id={}", id);
        return ResponseEntity.ok(publishService.publishAssessment(id));
    }

    // PUT /api/assessments/{id}/archive
    @PutMapping("/{id}/archive")
    public ResponseEntity<AssessmentResponse> archiveAssessment(@PathVariable Long id) {
        log.info("Request received: Archive Assessment - id={}", id);
        return ResponseEntity.ok(publishService.archiveAssessment(id));
    }
}
