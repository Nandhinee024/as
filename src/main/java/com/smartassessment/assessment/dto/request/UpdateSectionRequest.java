package com.smartassessment.assessment.dto.request;

import com.smartassessment.assessment.entity.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for updating an assessment section")
public class UpdateSectionRequest {

    @NotBlank(message = "Section title cannot be blank")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    @Schema(description = "Section title", example = "Section 1 - Core Java & OOP (MCQ)")
    private String title;

    @Schema(description = "Section description")
    private String description;

    @Min(value = 1, message = "Section order must be at least 1")
    @Schema(description = "Order in which section appears")
    private Integer sectionOrder;

    @Min(value = 1, message = "Section duration must be at least 1 minute")
    @Schema(description = "Optional section-specific timer in minutes")
    private Integer sectionDurationMinutes;

    @DecimalMin(value = "0.00", message = "Cutoff marks cannot be negative")
    @Schema(description = "Section-specific cutoff marks")
    private BigDecimal cutoffMarks;

    @NotNull(message = "Weightage percentage is required")
    @DecimalMin(value = "0.00", message = "Weightage must be >= 0%")
    @DecimalMax(value = "100.00", message = "Weightage cannot exceed 100%")
    @Schema(description = "Section contribution weightage percentage")
    private BigDecimal weightagePercentage;

    @NotNull(message = "Question type is required")
    @Schema(description = "Question Type (MCQ, MULTIPLE_SELECT, TEXT, CODING, DEBUGGING, MIXED)")
    private QuestionType questionType;

    @NotNull(message = "Total questions to ask is required")
    @Min(value = 1, message = "Must ask at least 1 question")
    @Schema(description = "Number of questions delivered from this section")
    private Integer totalQuestionsToAsk;

    @Min(value = 0, message = "Easy question count cannot be negative")
    @Schema(description = "Number of easy questions")
    private Integer easyQuestionCount;

    @Min(value = 0, message = "Medium question count cannot be negative")
    @Schema(description = "Number of medium questions")
    private Integer mediumQuestionCount;

    @Min(value = 0, message = "Hard question count cannot be negative")
    @Schema(description = "Number of hard questions")
    private Integer hardQuestionCount;

    @Schema(description = "Associated Question Bank / Question Set ID")
    private String questionSetId;
}
