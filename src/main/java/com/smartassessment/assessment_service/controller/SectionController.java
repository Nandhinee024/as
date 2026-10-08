package com.smartassessment.assessment_service.controller;

import com.smartassessment.assessment_service.dto.request.CreateSectionRequest;
import com.smartassessment.assessment_service.dto.request.UpdateSectionRequest;
import com.smartassessment.assessment_service.dto.response.SectionResponse;
import com.smartassessment.assessment_service.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
@Slf4j
public class SectionController {

    private final SectionService sectionService;

    // POST /api/sections
    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@Valid @RequestBody CreateSectionRequest request) {
        log.info("Request received: Create Section for assessmentId={}", request.getAssessmentId());
        return new ResponseEntity<>(sectionService.createSection(request), HttpStatus.CREATED);
    }

    // GET /api/sections/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SectionResponse> getSection(@PathVariable Long id) {
        log.info("Request received: Get Section - id={}", id);
        return ResponseEntity.ok(sectionService.getSectionById(id));
    }

    // GET /api/sections?assessmentId=1
    @GetMapping
    public ResponseEntity<List<SectionResponse>> getSectionsByAssessment(@RequestParam Long assessmentId) {
        log.info("Request received: Get Sections for assessmentId={}", assessmentId);
        return ResponseEntity.ok(sectionService.getSectionsByAssessmentId(assessmentId));
    }

    // PUT /api/sections/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SectionResponse> updateSection(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSectionRequest request) {
        log.info("Request received: Update Section - id={}", id);
        return ResponseEntity.ok(sectionService.updateSection(id, request));
    }

    // DELETE /api/sections/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        log.info("Request received: Delete Section - id={}", id);
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}
