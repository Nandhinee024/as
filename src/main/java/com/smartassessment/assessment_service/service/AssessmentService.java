package com.smartassessment.assessment_service.service;

import com.smartassessment.assessment_service.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment_service.dto.request.UpdateAssessmentRequest;
import com.smartassessment.assessment_service.dto.response.AssessmentResponse;

import java.util.List;

public interface AssessmentService {

    AssessmentResponse createAssessment(CreateAssessmentRequest request);

    AssessmentResponse getAssessmentById(Long id);

    List<AssessmentResponse> getAllAssessments();

    AssessmentResponse updateAssessment(Long id, UpdateAssessmentRequest request);

    void deleteAssessment(Long id);
}
