package com.smartassessment.assessment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_enrollments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_assessment_candidate", columnNames = {"assessment_id", "candidate_id"})
        },
        indexes = {
                @Index(name = "idx_enrollments_candidate", columnList = "candidate_id"),
                @Index(name = "idx_enrollments_token", columnList = "invitation_token"),
                @Index(name = "idx_enrollments_status", columnList = "enrollment_status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AssessmentEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(name = "candidate_id", nullable = false, length = 100)
    private String candidateId;

    @Column(name = "candidate_email", nullable = false, length = 255)
    private String candidateEmail;

    @Column(name = "candidate_name", nullable = false, length = 255)
    private String candidateName;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", nullable = false, length = 50)
    @Builder.Default
    private EnrollmentStatus enrollmentStatus = EnrollmentStatus.ENROLLED;

    @Column(name = "invitation_token", nullable = false, unique = true, length = 100)
    private String invitationToken;

    @Column(name = "scheduled_start_time")
    private LocalDateTime scheduledStartTime;

    @Column(name = "scheduled_end_time")
    private LocalDateTime scheduledEndTime;

    @Column(name = "attempt_count", nullable = false)
    @Builder.Default
    private Integer attemptCount = 0;

    @Column(name = "enrolled_by", nullable = false, length = 100)
    private String enrolledBy;

    @CreatedDate
    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
