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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Inter-service configuration payload for ExamSessionService and EvaluationService")
public class ExamSessionAssessmentConfigResponse {

    @Schema(description = "Assessment ID", example = "1")
    private Long assessmentId;

    @Schema(description = "Assessment Code", example = "JAVA-SR-2026")
    private String code;

    @Schema(description = "Assessment Title", example = "Java Senior Software Engineer Assessment")
    private String title;

    @Schema(description = "Status")
    private AssessmentStatus status;

    @Schema(description = "Access Type")
    private AccessType accessType;

    @Schema(description = "Total examination duration in minutes", example = "90")
    private Integer totalDurationMinutes;

    @Schema(description = "Total marks possible", example = "100.00")
    private BigDecimal totalMarks;

    @Schema(description = "Passing marks", example = "60.00")
    private BigDecimal passingMarks;

    @Schema(description = "Passing percentage", example = "60.00")
    private BigDecimal passingPercentage;

    @Schema(description = "Shuffle questions order")
    private Boolean shuffleQuestions;

    @Schema(description = "Shuffle option choices")
    private Boolean shuffleOptions;

    @Schema(description = "Allow candidate back navigation")
    private Boolean allowNavigationBack;

    @Schema(description = "Allow review mode")
    private Boolean allowReview;

    @Schema(description = "Allow candidate retake")
    private Boolean allowRetake;

    @Schema(description = "Max retake attempts")
    private Integer maxRetakeAttempts;

    @Schema(description = "Auto submit on timer expiration")
    private Boolean autoSubmitOnExpiry;

    @Schema(description = "Connection loss grace period in minutes", example = "10")
    private Integer connectionGracePeriodMinutes;

    @Schema(description = "Dynamic questionnaire AI enabled")
    private Boolean dynamicQuestionnaireEnabled;

    @Schema(description = "Show results immediately upon submission")
    private Boolean showResultsImmediately;

    @Schema(description = "Assessment availability window start")
    private LocalDateTime validFrom;

    @Schema(description = "Assessment availability window end")
    private LocalDateTime validTo;

    @Schema(description = "Ordered assessment sections")
    private List<SectionResponse> sections;

    @Schema(description = "Proctoring rules")
    private ProctoringRuleResponse proctoringRule;
}
