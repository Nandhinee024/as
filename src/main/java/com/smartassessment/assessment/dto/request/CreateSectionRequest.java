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
@Schema(description = "Request body for adding a section to an assessment")
public class CreateSectionRequest {

    @NotBlank(message = "Section title is required")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    @Schema(description = "Section title", example = "Section 1 - Core Java (MCQ)")
    private String title;

    @Schema(description = "Section description", example = "Questions covering Java threads, OOP, and JVM internals")
    private String description;

    @Min(value = 1, message = "Section order must be at least 1")
    @Schema(description = "Order in which section appears", example = "1")
    private Integer sectionOrder;

    @Min(value = 1, message = "Section duration must be at least 1 minute")
    @Schema(description = "Optional section-specific timer in minutes", example = "30")
    private Integer sectionDurationMinutes;

    @DecimalMin(value = "0.00", message = "Cutoff marks cannot be negative")
    @Schema(description = "Section-specific cutoff marks", example = "15.00")
    private BigDecimal cutoffMarks;

    @NotNull(message = "Weightage percentage is required")
    @DecimalMin(value = "0.00", message = "Weightage must be >= 0%")
    @DecimalMax(value = "100.00", message = "Weightage cannot exceed 100%")
    @Schema(description = "Section contribution weightage percentage", example = "30.00")
    private BigDecimal weightagePercentage;

    @NotNull(message = "Question type is required")
    @Schema(description = "Question Type (MCQ, MULTIPLE_SELECT, TEXT, CODING, DEBUGGING, MIXED)", example = "MCQ")
    private QuestionType questionType;

    @NotNull(message = "Total questions to ask is required")
    @Min(value = 1, message = "Must ask at least 1 question")
    @Schema(description = "Number of questions delivered from this section", example = "15")
    private Integer totalQuestionsToAsk;

    @Min(value = 0, message = "Easy question count cannot be negative")
    @Schema(description = "Number of easy questions", example = "5")
    @Builder.Default
    private Integer easyQuestionCount = 0;

    @Min(value = 0, message = "Medium question count cannot be negative")
    @Schema(description = "Number of medium questions", example = "7")
    @Builder.Default
    private Integer mediumQuestionCount = 0;

    @Min(value = 0, message = "Hard question count cannot be negative")
    @Schema(description = "Number of hard questions", example = "3")
    @Builder.Default
    private Integer hardQuestionCount = 0;

    @Schema(description = "Associated Question Bank / Question Set ID", example = "QSET-JAVA-MCQ-01")
    private String questionSetId;
}
