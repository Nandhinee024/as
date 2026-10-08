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
@Schema(description = "Candidate assessment access and entry verification response")
public class CandidateAssessmentAccessResponse {

    @Schema(description = "Assessment ID", example = "1")
    private Long assessmentId;

    @Schema(description = "Assessment Title", example = "Java Senior Software Engineer Assessment")
    private String title;

    @Schema(description = "Assessment Code", example = "JAVA-SR-2026")
    private String code;

    @Schema(description = "Assessment description")
    private String description;

    @Schema(description = "Candidate examination instructions")
    private String instructions;

    @Schema(description = "Category")
    private String category;

    @Schema(description = "Assessment status")
    private AssessmentStatus status;

    @Schema(description = "Access type")
    private AccessType accessType;

    @Schema(description = "Total exam duration in minutes", example = "90")
    private Integer totalDurationMinutes;

    @Schema(description = "Total marks possible", example = "100.00")
    private BigDecimal totalMarks;

    @Schema(description = "Passing marks requirement", example = "60.00")
    private BigDecimal passingMarks;

    @Schema(description = "Candidate ID", example = "cand-1001")
    private String candidateId;

    @Schema(description = "Candidate Email", example = "jane.doe@example.com")
    private String candidateEmail;

    @Schema(description = "Candidate Name", example = "Jane Doe")
    private String candidateName;

    @Schema(description = "Invitation Token", example = "token-jane-doe-java-2026")
    private String invitationToken;

    @Schema(description = "Scheduled start time")
    private LocalDateTime scheduledStartTime;

    @Schema(description = "Scheduled end time")
    private LocalDateTime scheduledEndTime;

    @Schema(description = "Whether passcode verification is required before starting", example = "false")
    private Boolean passcodeRequired;

    @Schema(description = "Whether candidate is currently eligible to start the exam", example = "true")
    private Boolean isEligibleToStart;

    @Schema(description = "Reason message if not eligible to start")
    private String eligibilityMessage;

    @Schema(description = "List of section overviews")
    private List<SectionOverview> sectionOverviews;

    @Schema(description = "Proctoring hardware and environment requirements")
    private ProctoringRequirements proctoringRequirements;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionOverview {
        private Long sectionId;
        private String title;
        private Integer sectionOrder;
        private Integer totalQuestions;
        private String questionType;
        private Integer sectionDurationMinutes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProctoringRequirements {
        private Boolean cameraRequired;
        private Boolean micRequired;
        private Boolean fullscreenEnforced;
        private Boolean faceDetectionRequired;
        private Boolean copyPasteForbidden;
        private Integer tabSwitchLimit;
    }
}
