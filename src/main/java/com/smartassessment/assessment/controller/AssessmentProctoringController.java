package com.smartassessment.assessment.controller;

import com.smartassessment.assessment.config.security.UserPrincipal;
import com.smartassessment.assessment.dto.request.UpdateProctoringRulesRequest;
import com.smartassessment.assessment.dto.response.ApiResponse;
import com.smartassessment.assessment.dto.response.ProctoringRuleResponse;
import com.smartassessment.assessment.service.ProctoringRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessments/admin/{assessmentId}/proctoring-rules")
@RequiredArgsConstructor
@Tag(name = "Assessment Proctoring Configuration", description = "Endpoints for configuring anti-cheating, webcam, mic, tab-switching, and AI monitoring rules")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AssessmentProctoringController {

    private final ProctoringRuleService proctoringRuleService;

    @GetMapping
    @Operation(summary = "Get proctoring rules", description = "Retrieves configured proctoring parameters for an assessment")
    public ResponseEntity<ApiResponse<ProctoringRuleResponse>> getProctoringRules(
            @PathVariable Long assessmentId) {
        ProctoringRuleResponse response = proctoringRuleService.getProctoringRulesByAssessmentId(assessmentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping
    @Operation(summary = "Update proctoring rules", description = "Updates camera, microphone, full-screen, tab switch limits, and face verification flags")
    public ResponseEntity<ApiResponse<ProctoringRuleResponse>> updateProctoringRules(
            @PathVariable Long assessmentId,
            @Valid @RequestBody UpdateProctoringRulesRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        ProctoringRuleResponse response = proctoringRuleService.updateProctoringRules(assessmentId, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Proctoring rules updated successfully", response));
    }
}
