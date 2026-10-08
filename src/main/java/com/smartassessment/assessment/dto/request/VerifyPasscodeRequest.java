package com.smartassessment.assessment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for candidate passcode verification")
public class VerifyPasscodeRequest {

    @NotBlank(message = "Passcode is required")
    @Schema(description = "Passcode provided by candidate", example = "SECRET2026")
    private String passcode;

    @NotBlank(message = "Candidate ID is required")
    @Schema(description = "Candidate identifier", example = "cand-1001")
    private String candidateId;
}
