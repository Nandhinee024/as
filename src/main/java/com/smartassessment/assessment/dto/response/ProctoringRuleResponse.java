package com.smartassessment.assessment.dto.response;

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
@Schema(description = "Assessment Proctoring Rules configuration response")
public class ProctoringRuleResponse {

    @Schema(description = "Proctoring rule record ID", example = "1")
    private Long id;

    @Schema(description = "Parent Assessment ID", example = "10")
    private Long assessmentId;

    @Schema(description = "Whether camera video feed is mandatory", example = "true")
    private Boolean cameraRequired;

    @Schema(description = "Whether microphone audio feed is mandatory", example = "true")
    private Boolean micRequired;

    @Schema(description = "Whether browser must be locked in fullscreen", example = "true")
    private Boolean fullscreenEnforced;

    @Schema(description = "Maximum allowed tab switches / focus losses", example = "3")
    private Integer tabSwitchLimit;

    @Schema(description = "Whether AI face tracking is enabled", example = "true")
    private Boolean faceDetectionRequired;

    @Schema(description = "Whether multiple person detection raises an incident", example = "true")
    private Boolean multipleFacesForbidden;

    @Schema(description = "Whether smartphone / device detection is active", example = "true")
    private Boolean phoneDetectionForbidden;

    @Schema(description = "Whether clipboard copy/paste is blocked", example = "true")
    private Boolean copyPasteForbidden;

    @Schema(description = "Whether code plagiarism / similarity check is run", example = "true")
    private Boolean codeSimilarityCheck;

    @Schema(description = "Whether full desktop screen sharing is enforced", example = "false")
    private Boolean screenShareRequired;

    @Schema(description = "Whether severe violations automatically terminate the session", example = "true")
    private Boolean autoTerminateOnViolations;

    @Schema(description = "Warning count before proactive intervention or termination", example = "3")
    private Integer warningThreshold;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
