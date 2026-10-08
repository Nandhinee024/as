package com.smartassessment.assessment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Complete Assessment details including sections and proctoring rules")
public class AssessmentDetailResponse extends AssessmentResponse {

    @Schema(description = "Configured assessment sections")
    private List<SectionResponse> sections;

    @Schema(description = "Configured proctoring and integrity rules")
    private ProctoringRuleResponse proctoringRule;

    @Schema(description = "Total number of questions across all sections", example = "18")
    private Integer totalQuestionsConfigured;
}
