package com.smartassessment.assessment.service;

import com.smartassessment.assessment.dto.request.CreateSectionRequest;
import com.smartassessment.assessment.dto.request.UpdateSectionRequest;
import com.smartassessment.assessment.dto.response.SectionResponse;

import java.util.List;

public interface SectionService {

    SectionResponse addSection(Long assessmentId, CreateSectionRequest request, String userId);

    SectionResponse updateSection(Long assessmentId, Long sectionId, UpdateSectionRequest request, String userId);

    void deleteSection(Long assessmentId, Long sectionId, String userId);

    SectionResponse getSectionById(Long assessmentId, Long sectionId);

    List<SectionResponse> getSectionsByAssessmentId(Long assessmentId);

    List<SectionResponse> reorderSections(Long assessmentId, List<Long> sectionIds, String userId);
}
