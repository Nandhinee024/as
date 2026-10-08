package com.smartassessment.assessment_service.dto.response;

import com.smartassessment.assessment_service.enums.AssessmentDifficulty;
import com.smartassessment.assessment_service.enums.AssessmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssessmentResponse {

    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Integer totalMarks;
    private AssessmentStatus status;
    private AssessmentDifficulty difficulty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> instructions;
    private List<SectionResponse> sections;
}
