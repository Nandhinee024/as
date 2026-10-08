package com.smartassessment.assessment_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamConfigResponse {

    private Long assessmentId;

    // Passing Criteria
    private BigDecimal passingPercentage;
    private Integer passingMarks;

    // Proctoring Rules
    private Boolean isCameraRequired;
    private Boolean isScreenShare;
    private Integer maxTabSwitches;
}
