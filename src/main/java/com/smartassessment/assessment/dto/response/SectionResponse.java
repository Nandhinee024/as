package com.smartassessment.assessment.dto.response;

import com.smartassessment.assessment.entity.QuestionType;
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
@Schema(description = "Assessment Section details")
public class SectionResponse {

    @Schema(description = "Section ID", example = "1")
    private Long id;

    @Schema(description = "Parent Assessment ID", example = "10")
    private Long assessmentId;

    @Schema(description = "Section title", example = "Section 1 - Core Java (MCQ)")
    private String title;

    @Schema(description = "Section description")
    private String description;

    @Schema(description = "Section display sequence", example = "1")
    private Integer sectionOrder;

    @Schema(description = "Section duration in minutes if section timer enabled", example = "30")
    private Integer sectionDurationMinutes;

    @Schema(description = "Section cutoff marks")
    private BigDecimal cutoffMarks;

    @Schema(description = "Contribution weightage percentage", example = "30.00")
    private BigDecimal weightagePercentage;

    @Schema(description = "Question Type")
    private QuestionType questionType;

    @Schema(description = "Total questions asked from this section", example = "15")
    private Integer totalQuestionsToAsk;

    @Schema(description = "Easy question count", example = "5")
    private Integer easyQuestionCount;

    @Schema(description = "Medium question count", example = "7")
    private Integer mediumQuestionCount;

    @Schema(description = "Hard question count", example = "3")
    private Integer hardQuestionCount;

    @Schema(description = "Associated Question Bank / Set ID", example = "QSET-JAVA-MCQ-01")
    private String questionSetId;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
