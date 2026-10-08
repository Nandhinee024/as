package com.smartassessment.assessment_service.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProctoringRuleRequest {

    private Boolean isCameraRequired = false;

    private Boolean isScreenShare = false;

    @Min(value = 0, message = "Max tab switches cannot be negative")
    private Integer maxTabSwitches = 0;
}
