package com.smartassessment.assessment.controller;

import com.smartassessment.assessment.dto.response.ApiResponse;
import com.smartassessment.assessment.dto.response.ExamSessionAssessmentConfigResponse;
import com.smartassessment.assessment.service.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessments/internal")
@RequiredArgsConstructor
@Tag(name = "Internal Service APIs", description = "Internal contract APIs consumed by ExamSessionService, SubmissionService, and EvaluationService")
public class AssessmentInternalController {

    private final AssessmentService assessmentService;

    @GetMapping("/{id}/config")
    @Operation(summary = "Get full exam configuration for Exam Session Service", description = "Returns complete examination rules, timers, sections, questions distribution, and proctoring constraints")
    public ResponseEntity<ApiResponse<ExamSessionAssessmentConfigResponse>> getAssessmentConfig(
            @PathVariable Long id,
            @RequestParam(required = false) String candidateId) {
        ExamSessionAssessmentConfigResponse response = assessmentService.getAssessmentConfigForExamSession(id, candidateId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
