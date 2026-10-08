package com.smartassessment.assessment.service;

import com.smartassessment.assessment.dto.response.AssessmentAuditLogResponse;
import com.smartassessment.assessment.dto.response.PagedResponse;
import com.smartassessment.assessment.entity.AuditAction;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {

    void logAction(
            Long assessmentId,
            String userId,
            String userRole,
            AuditAction action,
            String description,
            Object oldValue,
            Object newValue,
            String ipAddress
    );

    PagedResponse<AssessmentAuditLogResponse> getAuditLogsByAssessment(Long assessmentId, Pageable pageable);
}
