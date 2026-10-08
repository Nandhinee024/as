package com.smartassessment.assessment.controller;

import com.smartassessment.assessment.config.security.UserPrincipal;
import com.smartassessment.assessment.dto.request.AssessmentSearchCriteria;
import com.smartassessment.assessment.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment.dto.request.PublishAssessmentRequest;
import com.smartassessment.assessment.dto.request.UpdateAssessmentRequest;
import com.smartassessment.assessment.dto.response.*;
import com.smartassessment.assessment.service.AssessmentService;
import com.smartassessment.assessment.service.AuditLogService;
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

@RestController
@RequestMapping("/api/assessments/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Assessment Management", description = "Endpoints for administrators to create, configure, update, publish, and manage assessments")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AssessmentAdminController {

    private final AssessmentService assessmentService;
    private final AuditLogService auditLogService;

    @PostMapping
    @Operation(summary = "Create a new assessment", description = "Creates a new assessment in DRAFT status with default proctoring rules")
    public ResponseEntity<ApiResponse<AssessmentDetailResponse>> createAssessment(
            @Valid @RequestBody CreateAssessmentRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        AssessmentDetailResponse response = assessmentService.createAssessment(request, userId);
        return new ResponseEntity<>(ApiResponse.success("Assessment created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Search and list assessments", description = "Retrieves paginated assessments with flexible filtering by query, category, status, access type, and dates")
    public ResponseEntity<ApiResponse<PagedResponse<AssessmentSummaryResponse>>> searchAssessments(
            @Parameter(description = "Keyword search in title or code") @RequestParam(required = false) String query,
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by category") @RequestParam(required = false) String category,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(defaultValue = "DESC") String direction) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        AssessmentSearchCriteria criteria = AssessmentSearchCriteria.builder()
                .query(query)
                .category(category)
                .build();

        PagedResponse<AssessmentSummaryResponse> result = assessmentService.searchAssessments(criteria, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get assessment by ID", description = "Retrieves complete assessment details including sections and proctoring configuration")
    public ResponseEntity<ApiResponse<AssessmentDetailResponse>> getAssessmentById(
            @PathVariable Long id) {
        AssessmentDetailResponse response = assessmentService.getAssessmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get assessment by Code", description = "Retrieves assessment details by unique code identifier")
    public ResponseEntity<ApiResponse<AssessmentDetailResponse>> getAssessmentByCode(
            @PathVariable String code) {
        AssessmentDetailResponse response = assessmentService.getAssessmentByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an assessment", description = "Updates assessment configurations, timing, instructions, and rules")
    public ResponseEntity<ApiResponse<AssessmentDetailResponse>> updateAssessment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAssessmentRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        AssessmentDetailResponse response = assessmentService.updateAssessment(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Assessment updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete assessment", description = "Marks assessment as deleted and archived")
    public ResponseEntity<ApiResponse<Void>> deleteAssessment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        assessmentService.deleteAssessment(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Assessment deleted successfully", null));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish an assessment", description = "Validates sections & question configuration, and transitions assessment to PUBLISHED state")
    public ResponseEntity<ApiResponse<AssessmentDetailResponse>> publishAssessment(
            @PathVariable Long id,
            @RequestBody(required = false) PublishAssessmentRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        AssessmentDetailResponse response = assessmentService.publishAssessment(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Assessment published successfully", response));
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive an assessment", description = "Transitions assessment to ARCHIVED state")
    public ResponseEntity<ApiResponse<AssessmentDetailResponse>> archiveAssessment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        AssessmentDetailResponse response = assessmentService.archiveAssessment(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Assessment archived successfully", response));
    }

    @GetMapping("/{id}/audit-logs")
    @Operation(summary = "Get audit logs for assessment", description = "Retrieves complete audit trail history for an assessment")
    public ResponseEntity<ApiResponse<PagedResponse<AssessmentAuditLogResponse>>> getAuditLogs(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        PagedResponse<AssessmentAuditLogResponse> response = auditLogService.getAuditLogsByAssessment(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
