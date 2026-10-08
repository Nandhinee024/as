package com.smartassessment.assessment_service.service;

import com.smartassessment.assessment_service.dto.request.ConfigurationRequest;
import com.smartassessment.assessment_service.dto.response.ExamConfigResponse;

public interface ConfigurationService {

    ExamConfigResponse saveConfiguration(ConfigurationRequest request);

    ExamConfigResponse getConfiguration(Long assessmentId);
}
