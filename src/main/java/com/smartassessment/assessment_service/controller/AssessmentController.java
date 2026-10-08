package com.smartassessment.assessment_service.controller;

import com.smartassessment.assessment_service.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment_service.dto.request.UpdateAssessmentRequest;
import com.smartassessment.assessment_service.dto.response.AssessmentResponse;
import com.smartassessment.assessment_service.service.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
@Slf4j
public class AssessmentController {

    private final AssessmentService assessmentService;

    // POST /api/assessments
    @PostMapping
    public ResponseEntity<AssessmentResponse> createAssessment(@Valid @RequestBody CreateAssessmentRequest request) {
        log.info("Request received: Create Assessment - {}", request.getTitle());
        AssessmentResponse response = assessmentService.createAssessment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/assessments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponse> getAssessment(@PathVariable Long id) {
        log.info("Request received: Get Assessment - id={}", id);
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }

    // GET /api/assessments
    @GetMapping
    public ResponseEntity<List<AssessmentResponse>> getAllAssessments() {
        log.info("Request received: Get All Assessments");
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }

    // PUT /api/assessments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AssessmentResponse> updateAssessment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssessmentRequest request) {
        log.info("Request received: Update Assessment - id={}", id);
        return ResponseEntity.ok(assessmentService.updateAssessment(id, request));
    }

    // DELETE /api/assessments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        log.info("Request received: Delete Assessment - id={}", id);
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }
}
