package com.smartassessment.assessment.mapper;

import com.smartassessment.assessment.dto.response.AssessmentAuditLogResponse;
import com.smartassessment.assessment.entity.AssessmentAuditLog;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditLogMapper {

    public AssessmentAuditLogResponse toResponse(AssessmentAuditLog log) {
        if (log == null) {
            return null;
        }
        return AssessmentAuditLogResponse.builder()
                .id(log.getId())
                .assessmentId(log.getAssessmentId())
                .userId(log.getUserId())
                .userRole(log.getUserRole())
                .action(log.getAction())
                .description(log.getDescription())
                .oldValueJson(log.getOldValueJson())
                .newValueJson(log.getNewValueJson())
                .ipAddress(log.getIpAddress())
                .timestamp(log.getTimestamp())
                .build();
    }

    public List<AssessmentAuditLogResponse> toResponseList(List<AssessmentAuditLog> logs) {
        if (logs == null) {
            return List.of();
        }
        return logs.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
