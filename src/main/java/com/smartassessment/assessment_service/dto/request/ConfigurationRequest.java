package com.smartassessment.assessment_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfigurationRequest {

    @NotNull(message = "Assessment ID is required")
    private Long assessmentId;

    @Valid
    private PassingCriteriaRequest passingCriteria;

    @Valid
    private ProctoringRuleRequest proctoringRule;
}
