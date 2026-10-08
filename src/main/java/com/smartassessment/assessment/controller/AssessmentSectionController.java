package com.smartassessment.assessment.controller;

import com.smartassessment.assessment.config.security.UserPrincipal;
import com.smartassessment.assessment.dto.request.CreateSectionRequest;
import com.smartassessment.assessment.dto.request.UpdateSectionRequest;
import com.smartassessment.assessment.dto.response.ApiResponse;
import com.smartassessment.assessment.dto.response.SectionResponse;
import com.smartassessment.assessment.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments/admin/{assessmentId}/sections")
@RequiredArgsConstructor
@Tag(name = "Assessment Sections", description = "Endpoints for managing assessment sections and question distribution rules")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AssessmentSectionController {

    private final SectionService sectionService;

    @PostMapping
    @Operation(summary = "Add section to assessment", description = "Appends a new section (MCQ, Coding, Debugging, Text) to the assessment")
    public ResponseEntity<ApiResponse<SectionResponse>> addSection(
            @PathVariable Long assessmentId,
            @Valid @RequestBody CreateSectionRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        SectionResponse response = sectionService.addSection(assessmentId, request, userId);
        return new ResponseEntity<>(ApiResponse.success("Section added successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all sections for an assessment", description = "Retrieves all sections in execution order")
    public ResponseEntity<ApiResponse<List<SectionResponse>>> getSections(
            @PathVariable Long assessmentId) {
        List<SectionResponse> response = sectionService.getSectionsByAssessmentId(assessmentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{sectionId}")
    @Operation(summary = "Get single section details", description = "Retrieves details of a specific section")
    public ResponseEntity<ApiResponse<SectionResponse>> getSectionById(
            @PathVariable Long assessmentId,
            @PathVariable Long sectionId) {
        SectionResponse response = sectionService.getSectionById(assessmentId, sectionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{sectionId}")
    @Operation(summary = "Update section details", description = "Modifies section parameters, duration, cutoff marks, or question counts")
    public ResponseEntity<ApiResponse<SectionResponse>> updateSection(
            @PathVariable Long assessmentId,
            @PathVariable Long sectionId,
            @Valid @RequestBody UpdateSectionRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        SectionResponse response = sectionService.updateSection(assessmentId, sectionId, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Section updated successfully", response));
    }

    @DeleteMapping("/{sectionId}")
    @Operation(summary = "Delete a section", description = "Removes section from assessment")
    public ResponseEntity<ApiResponse<Void>> deleteSection(
            @PathVariable Long assessmentId,
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        sectionService.deleteSection(assessmentId, sectionId, userId);
        return ResponseEntity.ok(ApiResponse.success("Section deleted successfully", null));
    }

    @PutMapping("/reorder")
    @Operation(summary = "Reorder sections", description = "Updates execution sequence order for sections")
    public ResponseEntity<ApiResponse<List<SectionResponse>>> reorderSections(
            @PathVariable Long assessmentId,
            @RequestBody List<Long> sectionIds,
            @AuthenticationPrincipal UserPrincipal principal) {
        String userId = principal != null ? principal.getUserId() : "ADMIN";
        List<SectionResponse> response = sectionService.reorderSections(assessmentId, sectionIds, userId);
        return ResponseEntity.ok(ApiResponse.success("Sections reordered successfully", response));
    }
}
