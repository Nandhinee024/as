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
@Schema(description = "Assessment summary overview")
public class AssessmentSummaryResponse {

    @Schema(description = "Assessment ID", example = "1")
    private Long id;

    @Schema(description = "Assessment Title", example = "Java Senior Software Engineer Assessment")
    private String title;

    @Schema(description = "Assessment Code", example = "JAVA-SR-2026")
    private String code;

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

    @Schema(description = "Number of sections configured", example = "3")
    private Integer sectionCount;

    @Schema(description = "Total enrolled candidates count", example = "25")
    private Integer enrollmentCount;

    @Schema(description = "Valid from date")
    private LocalDateTime validFrom;

    @Schema(description = "Valid to date")
    private LocalDateTime validTo;

    @Schema(description = "Created by User ID", example = "admin-101")
    private String createdBy;

    @Schema(description = "Created timestamp")
    private LocalDateTime createdAt;
}
