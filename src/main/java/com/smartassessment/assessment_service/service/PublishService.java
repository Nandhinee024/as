package com.smartassessment.assessment_service.service;

import com.smartassessment.assessment_service.dto.response.AssessmentResponse;

public interface PublishService {

    AssessmentResponse publishAssessment(Long assessmentId);

    AssessmentResponse archiveAssessment(Long assessmentId);
}
