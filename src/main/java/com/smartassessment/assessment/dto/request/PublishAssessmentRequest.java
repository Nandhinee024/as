package com.smartassessment.assessment.dto.request;

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
@Schema(description = "Request body for publishing an assessment")
public class PublishAssessmentRequest {

    @Schema(description = "Assessment availability start time")
    private LocalDateTime validFrom;

    @Schema(description = "Assessment availability end time")
    private LocalDateTime validTo;

    @Schema(description = "Optional notification comment for enrolled candidates")
    private String comment;
}
