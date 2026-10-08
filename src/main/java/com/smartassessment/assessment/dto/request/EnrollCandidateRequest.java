package com.smartassessment.assessment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for enrolling a candidate into an assessment")
public class EnrollCandidateRequest {

    @NotBlank(message = "Candidate ID is required")
    @Schema(description = "Unique identifier of candidate in Identity Service", example = "cand-1001")
    private String candidateId;

    @NotBlank(message = "Candidate email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Candidate Email", example = "jane.doe@example.com")
    private String candidateEmail;

    @NotBlank(message = "Candidate name is required")
    @Schema(description = "Candidate Full Name", example = "Jane Doe")
    private String candidateName;

    @Schema(description = "Specific window start time override for this candidate")
    private LocalDateTime scheduledStartTime;

    @Schema(description = "Specific window end time override for this candidate")
    private LocalDateTime scheduledEndTime;

    @Schema(description = "Optional custom token expiry")
    private LocalDateTime expiresAt;
}
