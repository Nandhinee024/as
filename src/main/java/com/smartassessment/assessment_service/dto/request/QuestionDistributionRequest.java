package com.smartassessment.assessment_service.dto.request;

import com.smartassessment.assessment_service.enums.AssessmentDifficulty;
import com.smartassessment.assessment_service.enums.DistributionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionDistributionRequest {

    private Long topicId;

    @NotNull(message = "Number of questions is required")
    @Min(value = 1, message = "At least 1 question required")
    private Integer numQuestions;

    private AssessmentDifficulty difficulty;

    @NotNull(message = "Distribution type is required")
    private DistributionType distributionType;
}
