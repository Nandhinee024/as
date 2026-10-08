package com.smartassessment.assessment.dto.request;

import com.smartassessment.assessment.entity.AccessType;
import com.smartassessment.assessment.entity.AssessmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Filter and search parameters for assessments")
public class AssessmentSearchCriteria {

    @Schema(description = "Keyword search in title, code, or description")
    private String query;

    @Schema(description = "Filter by assessment status")
    private AssessmentStatus status;

    @Schema(description = "Filter by category")
    private String category;

    @Schema(description = "Filter by access type")
    private AccessType accessType;

    @Schema(description = "Filter by creator admin user ID")
    private String createdBy;

    @Schema(description = "Filter assessments valid after this timestamp")
    private LocalDateTime validFrom;

    @Schema(description = "Filter assessments valid before this timestamp")
    private LocalDateTime validTo;
}
