package com.smartassessment.assessment.dto.request;

import com.smartassessment.assessment.entity.AccessType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating a new assessment")
public class CreateAssessmentRequest {

    @NotBlank(message = "Assessment title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @Schema(description = "Assessment Title", example = "Java Senior Software Engineer Assessment")
    private String title;

    @NotBlank(message = "Assessment code is required")
    @Pattern(regexp = "^[A-Z0-9_-]{3,50}$", message = "Code must contain only uppercase alphanumeric characters, underscores, and hyphens")
    @Schema(description = "Unique Assessment Code identifier", example = "JAVA-SR-2026")
    private String code;

    @Schema(description = "Detailed assessment description", example = "Assessment testing Java 21, Spring Boot, algorithms, and debugging.")
    private String description;

    @Schema(description = "Instructions shown to the candidate before starting the exam", example = "1. Maintain video presence. 2. Autosave is active.")
    private String instructions;

    @NotBlank(message = "Category is required")
    @Schema(description = "Assessment category", example = "Software Engineering")
    private String category;

    @Schema(description = "Access Type (PUBLIC, INVITE_ONLY, PASSWORD_PROTECTED)", example = "INVITE_ONLY")
    @Builder.Default
    private AccessType accessType = AccessType.INVITE_ONLY;

    @Schema(description = "Passcode required if accessType is PASSWORD_PROTECTED", example = "SECRET2026")
    private String passcode;

    @NotNull(message = "Total duration in minutes is required")
    @Min(value = 5, message = "Duration must be at least 5 minutes")
    @Max(value = 600, message = "Duration cannot exceed 600 minutes")
    @Schema(description = "Total exam duration in minutes", example = "90")
    private Integer totalDurationMinutes;

    @NotNull(message = "Total marks is required")
    @DecimalMin(value = "1.00", message = "Total marks must be greater than 0")
    @Schema(description = "Total marks possible", example = "100.00")
    private BigDecimal totalMarks;

    @NotNull(message = "Passing marks is required")
    @DecimalMin(value = "0.00", message = "Passing marks cannot be negative")
    @Schema(description = "Passing marks cutoff", example = "60.00")
    private BigDecimal passingMarks;

    @Schema(description = "Passing score percentage", example = "60.00")
    private BigDecimal passingPercentage;

    @Schema(description = "Start of assessment availability window")
    private LocalDateTime validFrom;

    @Schema(description = "End of assessment availability window")
    private LocalDateTime validTo;

    @Schema(description = "Whether to randomize question order per candidate", example = "true")
    @Builder.Default
    private Boolean shuffleQuestions = true;

    @Schema(description = "Whether to shuffle option choices for MCQs", example = "true")
    @Builder.Default
    private Boolean shuffleOptions = true;

    @Schema(description = "Whether candidate can navigate back to previous questions", example = "true")
    @Builder.Default
    private Boolean allowNavigationBack = true;

    @Schema(description = "Whether candidate can review questions before submitting", example = "true")
    @Builder.Default
    private Boolean allowReview = true;

    @Schema(description = "Whether retake is allowed", example = "false")
    @Builder.Default
    private Boolean allowRetake = false;

    @Min(value = 1, message = "Max retake attempts must be at least 1")
    @Schema(description = "Max retake attempts allowed", example = "1")
    @Builder.Default
    private Integer maxRetakeAttempts = 1;

    @Schema(description = "Whether exam is automatically submitted when timer expires", example = "true")
    @Builder.Default
    private Boolean autoSubmitOnExpiry = true;

    @Min(value = 0, message = "Grace period cannot be negative")
    @Schema(description = "Network disconnection grace period in minutes", example = "10")
    @Builder.Default
    private Integer connectionGracePeriodMinutes = 10;

    @Schema(description = "Whether dynamic follow-up questionnaire AI is enabled", example = "true")
    @Builder.Default
    private Boolean dynamicQuestionnaireEnabled = false;

    @Schema(description = "Whether to show evaluation results immediately after submission", example = "false")
    @Builder.Default
    private Boolean showResultsImmediately = false;
}
