package com.smartassessment.assessment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_proctoring_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AssessmentProctoringRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false, unique = true)
    private Assessment assessment;

    @Column(name = "camera_required", nullable = false)
    @Builder.Default
    private Boolean cameraRequired = true;

    @Column(name = "mic_required", nullable = false)
    @Builder.Default
    private Boolean micRequired = true;

    @Column(name = "fullscreen_enforced", nullable = false)
    @Builder.Default
    private Boolean fullscreenEnforced = true;

    @Column(name = "tab_switch_limit", nullable = false)
    @Builder.Default
    private Integer tabSwitchLimit = 3;

    @Column(name = "face_detection_required", nullable = false)
    @Builder.Default
    private Boolean faceDetectionRequired = true;

    @Column(name = "multiple_faces_forbidden", nullable = false)
    @Builder.Default
    private Boolean multipleFacesForbidden = true;

    @Column(name = "phone_detection_forbidden", nullable = false)
    @Builder.Default
    private Boolean phoneDetectionForbidden = true;

    @Column(name = "copy_paste_forbidden", nullable = false)
    @Builder.Default
    private Boolean copyPasteForbidden = true;

    @Column(name = "code_similarity_check", nullable = false)
    @Builder.Default
    private Boolean codeSimilarityCheck = true;

    @Column(name = "screen_share_required", nullable = false)
    @Builder.Default
    private Boolean screenShareRequired = false;

    @Column(name = "auto_terminate_on_violations", nullable = false)
    @Builder.Default
    private Boolean autoTerminateOnViolations = true;

    @Column(name = "warning_threshold", nullable = false)
    @Builder.Default
    private Integer warningThreshold = 3;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
