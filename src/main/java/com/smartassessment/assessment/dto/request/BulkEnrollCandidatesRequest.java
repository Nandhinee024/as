package com.smartassessment.assessment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for bulk candidate enrollment")
public class BulkEnrollCandidatesRequest {

    @NotEmpty(message = "Candidate list cannot be empty")
    @Valid
    @Schema(description = "List of candidates to enroll")
    private List<EnrollCandidateRequest> candidates;

    @Schema(description = "Whether to trigger immediate email invitation notifications via Notification Service", example = "true")
    @Builder.Default
    private Boolean sendNotificationEmails = true;
}
