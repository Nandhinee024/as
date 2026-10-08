package com.smartassessment.assessment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_audit_logs", indexes = {
        @Index(name = "idx_audit_assessment_id", columnList = "assessment_id"),
        @Index(name = "idx_audit_user_id", columnList = "user_id"),
        @Index(name = "idx_audit_action", columnList = "action")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(name = "user_role", nullable = false, length = 50)
    private String userRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AuditAction action;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "old_value_json", columnDefinition = "LONGTEXT")
    private String oldValueJson;

    @Column(name = "new_value_json", columnDefinition = "LONGTEXT")
    private String newValueJson;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
