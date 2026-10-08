package com.smartassessment.assessment.controller;

import com.smartassessment.assessment.config.security.UserPrincipal;
import com.smartassessment.assessment.dto.request.VerifyPasscodeRequest;
import com.smartassessment.assessment.dto.response.ApiResponse;
import com.smartassessment.assessment.dto.response.CandidateAssessmentAccessResponse;
import com.smartassessment.assessment.dto.response.EnrollmentResponse;
import com.smartassessment.assessment.service.AssessmentService;
import com.smartassessment.assessment.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assessments/candidate")
@RequiredArgsConstructor
@Tag(name = "Candidate Assessment Access", description = "Endpoints for candidates to verify invitation tokens, check requirements, and view enrolled assessments")
public class AssessmentCandidateController {

    private final AssessmentService assessmentService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/verify-access/{invitationToken}")
    @Operation(summary = "Verify assessment access token", description = "Validates candidate invitation token, returns instructions, scheduled window, section list, and proctoring requirements")
    public ResponseEntity<ApiResponse<CandidateAssessmentAccessResponse>> verifyAccess(
            @PathVariable String invitationToken) {
        CandidateAssessmentAccessResponse response = assessmentService.verifyCandidateAccessByToken(invitationToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/verify-passcode")
    @Operation(summary = "Verify assessment passcode", description = "Validates passcode for password-protected assessments")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyPasscode(
            @PathVariable Long id,
            @Valid @RequestBody VerifyPasscodeRequest request) {
        boolean valid = assessmentService.verifyAssessmentPasscode(id, request.getCandidateId(), request.getPasscode());
        return ResponseEntity.ok(ApiResponse.success(Map.of("valid", valid)));
    }

    @GetMapping("/my-assessments")
    @Operation(summary = "Get candidate enrolled assessments", description = "Retrieves all assessments assigned to currently authenticated candidate")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getMyAssessments(
            @AuthenticationPrincipal UserPrincipal principal) {
        String candidateId = principal != null ? principal.getUserId() : "UNKNOWN";
        List<EnrollmentResponse> responses = enrollmentService.getEnrollmentsByCandidateId(candidateId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
