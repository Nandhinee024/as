package com.smartassessment.assessment.dto.response;

import com.smartassessment.assessment.entity.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Candidate Enrollment details")
public class EnrollmentResponse {

    @Schema(description = "Enrollment ID", example = "1")
    private Long id;

    @Schema(description = "Assessment ID", example = "10")
    private Long assessmentId;

    @Schema(description = "Assessment Title", example = "Java Senior Software Engineer Assessment")
    private String assessmentTitle;

    @Schema(description = "Candidate ID", example = "cand-1001")
    private String candidateId;

    @Schema(description = "Candidate Email", example = "jane.doe@example.com")
    private String candidateEmail;

    @Schema(description = "Candidate Name", example = "Jane Doe")
    private String candidateName;

    @Schema(description = "Enrollment status")
    private EnrollmentStatus enrollmentStatus;

    @Schema(description = "Unique Invitation Token for exam access", example = "token-jane-doe-java-2026")
    private String invitationToken;

    @Schema(description = "Candidate specific scheduled start time")
    private LocalDateTime scheduledStartTime;

    @Schema(description = "Candidate specific scheduled end time")
    private LocalDateTime scheduledEndTime;

    @Schema(description = "Attempts consumed so far", example = "0")
    private Integer attemptCount;

    @Schema(description = "Enrolled by Admin User ID", example = "admin-101")
    private String enrolledBy;

    @Schema(description = "Enrollment timestamp")
    private LocalDateTime enrolledAt;

    @Schema(description = "Token expiration timestamp")
    private LocalDateTime expiresAt;
}
