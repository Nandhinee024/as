package com.smartassessment.assessment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessments", indexes = {
        @Index(name = "idx_assessments_status", columnList = "status"),
        @Index(name = "idx_assessments_category", columnList = "category"),
        @Index(name = "idx_assessments_created_by", columnList = "created_by"),
        @Index(name = "idx_assessments_code", columnList = "code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String instructions;

    @Column(nullable = false, length = 100)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private AssessmentStatus status = AssessmentStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", nullable = false, length = 50)
    @Builder.Default
    private AccessType accessType = AccessType.INVITE_ONLY;

    @Column(length = 255)
    private String passcode;

    @Column(name = "total_duration_minutes", nullable = false)
    @Builder.Default
    private Integer totalDurationMinutes = 60;

    @Column(name = "total_marks", precision = 10, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalMarks = BigDecimal.valueOf(100.00);

    @Column(name = "passing_marks", precision = 10, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal passingMarks = BigDecimal.valueOf(40.00);

    @Column(name = "passing_percentage", precision = 5, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal passingPercentage = BigDecimal.valueOf(40.00);

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "shuffle_questions", nullable = false)
    @Builder.Default
    private Boolean shuffleQuestions = true;

    @Column(name = "shuffle_options", nullable = false)
    @Builder.Default
    private Boolean shuffleOptions = true;

    @Column(name = "allow_navigation_back", nullable = false)
    @Builder.Default
    private Boolean allowNavigationBack = true;

    @Column(name = "allow_review", nullable = false)
    @Builder.Default
    private Boolean allowReview = true;

    @Column(name = "allow_retake", nullable = false)
    @Builder.Default
    private Boolean allowRetake = false;

    @Column(name = "max_retake_attempts", nullable = false)
    @Builder.Default
    private Integer maxRetakeAttempts = 1;

    @Column(name = "auto_submit_on_expiry", nullable = false)
    @Builder.Default
    private Boolean autoSubmitOnExpiry = true;

    @Column(name = "connection_grace_period_minutes", nullable = false)
    @Builder.Default
    private Integer connectionGracePeriodMinutes = 10;

    @Column(name = "dynamic_questionnaire_enabled", nullable = false)
    @Builder.Default
    private Boolean dynamicQuestionnaireEnabled = false;

    @Column(name = "show_results_immediately", nullable = false)
    @Builder.Default
    private Boolean showResultsImmediately = false;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sectionOrder ASC")
    @Builder.Default
    private List<AssessmentSection> sections = new ArrayList<>();

    @OneToOne(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AssessmentProctoringRule proctoringRule;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AssessmentEnrollment> enrollments = new ArrayList<>();

    public void addSection(AssessmentSection section) {
        sections.add(section);
        section.setAssessment(this);
    }

    public void removeSection(AssessmentSection section) {
        sections.remove(section);
        section.setAssessment(null);
    }

    public void setProctoringRule(AssessmentProctoringRule proctoringRule) {
        this.proctoringRule = proctoringRule;
        if (proctoringRule != null) {
            proctoringRule.setAssessment(this);
        }
    }
}
