package com.smartassessment.assessment.controller;

import com.smartassessment.assessment.config.security.UserPrincipal;
import com.smartassessment.assessment.dto.request.BulkEnrollCandidatesRequest;
import com.smartassessment.assessment.dto.request.EnrollCandidateRequest;
import com.smartassessment.assessment.dto.response.ApiResponse;
import com.smartassessment.assessment.dto.response.EnrollmentResponse;
import com.smartassessment.assessment.dto.response.PagedResponse;
import com.smartassessment.assessment.entity.EnrollmentStatus;
import com.smartassessment.assessment.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments/admin/{assessmentId}/enrollments")
@RequiredArgsConstructor
@Tag(name = "Assessment Candidate Enrollment", description = "Endpoints for managing candidate invitations and rosters")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AssessmentEnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Enroll single candidate", description = "Enrolls candidate, generates invitation token, and dispatches invitation email")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enrollCandidate(
            @PathVariable Long assessmentId,
            @Valid @RequestBody EnrollCandidateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        EnrollmentResponse response = enrollmentService.enrollCandidate(assessmentId, request, userId);
        return new ResponseEntity<>(ApiResponse.success("Candidate enrolled successfully", response), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk enroll candidates", description = "Enrolls candidate roster and triggers invitations in bulk")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> bulkEnrollCandidates(
            @PathVariable Long assessmentId,
            @Valid @RequestBody BulkEnrollCandidatesRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        List<EnrollmentResponse> responses = enrollmentService.bulkEnrollCandidates(assessmentId, request, userId);
        return new ResponseEntity<>(ApiResponse.success(String.format("Successfully enrolled %d candidates", responses.size()), responses), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get enrolled candidates", description = "Retrieves paginated enrollment list with status filtering")
    public ResponseEntity<ApiResponse<PagedResponse<EnrollmentResponse>>> getEnrollments(
            @PathVariable Long assessmentId,
            @Parameter(description = "Filter by status") @RequestParam(required = false) EnrollmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "enrolledAt"));
        PagedResponse<EnrollmentResponse> response = enrollmentService.getEnrollmentsByAssessment(assessmentId, status, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{enrollmentId}/status")
    @Operation(summary = "Update enrollment status", description = "Updates status of candidate enrollment record")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> updateStatus(
            @PathVariable Long assessmentId,
            @PathVariable Long enrollmentId,
            @RequestParam EnrollmentStatus status,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        EnrollmentResponse response = enrollmentService.updateEnrollmentStatus(assessmentId, enrollmentId, status, userId);
        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", response));
    }

    @DeleteMapping("/{enrollmentId}")
    @Operation(summary = "Cancel candidate enrollment", description = "Cancels candidate access to assessment")
    public ResponseEntity<ApiResponse<Void>> cancelEnrollment(
            @PathVariable Long assessmentId,
            @PathVariable Long enrollmentId,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        enrollmentService.cancelEnrollment(assessmentId, enrollmentId, userId);
        return ResponseEntity.ok(ApiResponse.success("Enrollment cancelled successfully", null));
    }
}
