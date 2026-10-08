package com.smartassessment.assessment.mapper;

import com.smartassessment.assessment.dto.response.EnrollmentResponse;
import com.smartassessment.assessment.entity.AssessmentEnrollment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnrollmentMapper {

    public EnrollmentResponse toResponse(AssessmentEnrollment enrollment) {
        if (enrollment == null) {
            return null;
        }
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .assessmentId(enrollment.getAssessment() != null ? enrollment.getAssessment().getId() : null)
                .assessmentTitle(enrollment.getAssessment() != null ? enrollment.getAssessment().getTitle() : null)
                .candidateId(enrollment.getCandidateId())
                .candidateEmail(enrollment.getCandidateEmail())
                .candidateName(enrollment.getCandidateName())
                .enrollmentStatus(enrollment.getEnrollmentStatus())
                .invitationToken(enrollment.getInvitationToken())
                .scheduledStartTime(enrollment.getScheduledStartTime())
                .scheduledEndTime(enrollment.getScheduledEndTime())
                .attemptCount(enrollment.getAttemptCount())
                .enrolledBy(enrollment.getEnrolledBy())
                .enrolledAt(enrollment.getEnrolledAt())
                .expiresAt(enrollment.getExpiresAt())
                .build();
    }

    public List<EnrollmentResponse> toResponseList(List<AssessmentEnrollment> enrollments) {
        if (enrollments == null) {
            return List.of();
        }
        return enrollments.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
