package com.smartassessment.assessment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for configuring proctoring and anti-cheating rules")
public class UpdateProctoringRulesRequest {

    @NotNull(message = "Camera required flag cannot be null")
    @Schema(description = "Whether camera video feed is mandatory", example = "true")
    private Boolean cameraRequired;

    @NotNull(message = "Microphone required flag cannot be null")
    @Schema(description = "Whether microphone audio feed is mandatory", example = "true")
    private Boolean micRequired;

    @NotNull(message = "Fullscreen enforcement flag cannot be null")
    @Schema(description = "Whether browser must be locked in fullscreen", example = "true")
    private Boolean fullscreenEnforced;

    @NotNull(message = "Tab switch limit cannot be null")
    @Min(value = 0, message = "Tab switch limit cannot be negative")
    @Max(value = 10, message = "Tab switch limit cannot exceed 10")
    @Schema(description = "Maximum allowed tab switches / focus losses before termination", example = "3")
    private Integer tabSwitchLimit;

    @NotNull(message = "Face detection flag cannot be null")
    @Schema(description = "Whether AI face tracking is enabled", example = "true")
    private Boolean faceDetectionRequired;

    @NotNull(message = "Multiple faces forbidden flag cannot be null")
    @Schema(description = "Whether multiple person detection raises an incident", example = "true")
    private Boolean multipleFacesForbidden;

    @NotNull(message = "Phone detection flag cannot be null")
    @Schema(description = "Whether smartphone / device detection is active", example = "true")
    private Boolean phoneDetectionForbidden;

    @NotNull(message = "Copy paste forbidden flag cannot be null")
    @Schema(description = "Whether clipboard copy/paste is blocked", example = "true")
    private Boolean copyPasteForbidden;

    @NotNull(message = "Code similarity check flag cannot be null")
    @Schema(description = "Whether code plagiarism / similarity check is run", example = "true")
    private Boolean codeSimilarityCheck;

    @NotNull(message = "Screen share required flag cannot be null")
    @Schema(description = "Whether full desktop screen sharing is enforced", example = "false")
    private Boolean screenShareRequired;

    @NotNull(message = "Auto terminate flag cannot be null")
    @Schema(description = "Whether severe violations automatically terminate the session", example = "true")
    private Boolean autoTerminateOnViolations;

    @NotNull(message = "Warning threshold cannot be null")
    @Min(value = 1, message = "Warning threshold must be at least 1")
    @Schema(description = "Warning count before proactive intervention or termination", example = "3")
    private Integer warningThreshold;
}
