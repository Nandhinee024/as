package com.smartassessment.assessment.dto.response;

import com.smartassessment.assessment.entity.AccessType;
import com.smartassessment.assessment.entity.AssessmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Assessment response object")
public class AssessmentResponse {

    @Schema(description = "Assessment ID", example = "1")
    private Long id;

    @Schema(description = "Assessment Title", example = "Java Senior Software Engineer Assessment")
    private String title;

    @Schema(description = "Assessment Code", example = "JAVA-SR-2026")
    private String code;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Candidate Instructions")
    private String instructions;

    @Schema(description = "Category", example = "Software Engineering")
    private String category;

    @Schema(description = "Status")
    private AssessmentStatus status;

    @Schema(description = "Access Type")
    private AccessType accessType;

    @Schema(description = "Total duration in minutes", example = "90")
    private Integer totalDurationMinutes;

    @Schema(description = "Total marks", example = "100.00")
    private BigDecimal totalMarks;

    @Schema(description = "Passing marks", example = "60.00")
    private BigDecimal passingMarks;

    @Schema(description = "Passing score percentage", example = "60.00")
    private BigDecimal passingPercentage;

    @Schema(description = "Valid from date")
    private LocalDateTime validFrom;

    @Schema(description = "Valid to date")
    private LocalDateTime validTo;

    @Schema(description = "Question randomization flag")
    private Boolean shuffleQuestions;

    @Schema(description = "Option randomization flag")
    private Boolean shuffleOptions;

    @Schema(description = "Custom test navigation: back navigation flag")
    private Boolean allowNavigationBack;

    @Schema(description = "Review permitted flag")
    private Boolean allowReview;

    @Schema(description = "Retake permitted flag")
    private Boolean allowRetake;

    @Schema(description = "Max retake attempts")
    private Integer maxRetakeAttempts;

    @Schema(description = "Auto-submit on expiry flag")
    private Boolean autoSubmitOnExpiry;

    @Schema(description = "Network grace period in minutes")
    private Integer connectionGracePeriodMinutes;

    @Schema(description = "Dynamic questionnaire enabled flag")
    private Boolean dynamicQuestionnaireEnabled;

    @Schema(description = "Immediate result display flag")
    private Boolean showResultsImmediately;

    @Schema(description = "Created by admin User ID")
    private String createdBy;

    @Schema(description = "Updated by admin User ID")
    private String updatedBy;

    @Schema(description = "Created timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
