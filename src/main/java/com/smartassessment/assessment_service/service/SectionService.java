package com.smartassessment.assessment_service.service;

import com.smartassessment.assessment_service.dto.request.CreateSectionRequest;
import com.smartassessment.assessment_service.dto.request.UpdateSectionRequest;
import com.smartassessment.assessment_service.dto.response.SectionResponse;

import java.util.List;

public interface SectionService {

    SectionResponse createSection(CreateSectionRequest request);

    SectionResponse getSectionById(Long id);

    List<SectionResponse> getSectionsByAssessmentId(Long assessmentId);

    SectionResponse updateSection(Long id, UpdateSectionRequest request);

    void deleteSection(Long id);
}
