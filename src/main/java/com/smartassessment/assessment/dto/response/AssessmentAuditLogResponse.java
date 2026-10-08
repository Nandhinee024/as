package com.smartassessment.assessment.dto.response;

import com.smartassessment.assessment.entity.AuditAction;
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
@Schema(description = "Assessment Audit Log Response")
public class AssessmentAuditLogResponse {

    @Schema(description = "Audit Log ID", example = "1")
    private Long id;

    @Schema(description = "Assessment ID", example = "1")
    private Long assessmentId;

    @Schema(description = "User ID who performed the action", example = "admin-101")
    private String userId;

    @Schema(description = "Role of user", example = "ADMIN")
    private String userRole;

    @Schema(description = "Audit Action performed")
    private AuditAction action;

    @Schema(description = "Description of change", example = "Assessment published by admin-101")
    private String description;

    @Schema(description = "Previous state snapshot")
    private String oldValueJson;

    @Schema(description = "New state snapshot")
    private String newValueJson;

    @Schema(description = "Origin IP address")
    private String ipAddress;

    @Schema(description = "Timestamp when action occurred")
    private LocalDateTime timestamp;
}
