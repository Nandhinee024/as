package com.smartassessment.assessment_service.dto.request;

import com.smartassessment.assessment_service.enums.SectionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateSectionRequest {

    @NotNull(message = "Assessment ID is required")
    private Long assessmentId;

    @NotBlank(message = "Section name is required")
    private String sectionName;

    @NotNull(message = "Section type is required")
    private SectionType sectionType;

    @Min(value = 1, message = "Marks per question must be at least 1")
    private Integer marksPerQuestion;

    @Valid
    private List<QuestionDistributionRequest> questionDistributions;
}
