package com.smartassessment.assessment.dto.request;

import com.smartassessment.assessment.entity.AccessType;
import com.smartassessment.assessment.entity.AssessmentStatus;
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
@Schema(description = "Request body for updating an assessment")
public class UpdateAssessmentRequest {

    @NotBlank(message = "Assessment title cannot be blank")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    @Schema(description = "Assessment Title", example = "Java Senior Software Engineer Assessment (Updated)")
    private String title;

    @Schema(description = "Assessment description")
    private String description;

    @Schema(description = "Assessment instructions")
    private String instructions;

    @NotBlank(message = "Category cannot be blank")
    @Schema(description = "Assessment category", example = "Software Engineering")
    private String category;

    @Schema(description = "Assessment status")
    private AssessmentStatus status;

    @Schema(description = "Access Type")
    private AccessType accessType;

    @Schema(description = "Passcode")
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

    @Schema(description = "Whether to randomize question order per candidate")
    private Boolean shuffleQuestions;

    @Schema(description = "Whether to shuffle option choices for MCQs")
    private Boolean shuffleOptions;

    @Schema(description = "Whether candidate can navigate back to previous questions")
    private Boolean allowNavigationBack;

    @Schema(description = "Whether candidate can review questions before submitting")
    private Boolean allowReview;

    @Schema(description = "Whether retake is allowed")
    private Boolean allowRetake;

    @Min(value = 1, message = "Max retake attempts must be at least 1")
    @Schema(description = "Max retake attempts allowed")
    private Integer maxRetakeAttempts;

    @Schema(description = "Whether exam is automatically submitted when timer expires")
    private Boolean autoSubmitOnExpiry;

    @Min(value = 0, message = "Grace period cannot be negative")
    @Schema(description = "Network disconnection grace period in minutes")
    private Integer connectionGracePeriodMinutes;

    @Schema(description = "Whether dynamic follow-up questionnaire AI is enabled")
    private Boolean dynamicQuestionnaireEnabled;

    @Schema(description = "Whether to show evaluation results immediately after submission")
    private Boolean showResultsImmediately;
}
