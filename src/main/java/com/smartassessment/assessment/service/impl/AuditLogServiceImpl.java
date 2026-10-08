package com.smartassessment.assessment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartassessment.assessment.dto.response.AssessmentAuditLogResponse;
import com.smartassessment.assessment.dto.response.PagedResponse;
import com.smartassessment.assessment.entity.AssessmentAuditLog;
import com.smartassessment.assessment.entity.AuditAction;
import com.smartassessment.assessment.mapper.AuditLogMapper;
import com.smartassessment.assessment.repository.AssessmentAuditLogRepository;
import com.smartassessment.assessment.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AssessmentAuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final ObjectMapper objectMapper;

    @Override
    @Async
    @Transactional
    public void logAction(
            Long assessmentId,
            String userId,
            String userRole,
            AuditAction action,
            String description,
            Object oldValue,
            Object newValue,
            String ipAddress) {
        
        try {
            String oldJson = oldValue != null ? (oldValue instanceof String ? (String) oldValue : objectMapper.writeValueAsString(oldValue)) : null;
            String newJson = newValue != null ? (newValue instanceof String ? (String) newValue : objectMapper.writeValueAsString(newValue)) : null;

            AssessmentAuditLog auditLog = AssessmentAuditLog.builder()
                    .assessmentId(assessmentId)
                    .userId(userId != null ? userId : "SYSTEM")
                    .userRole(userRole != null ? userRole : "SYSTEM")
                    .action(action)
                    .description(description)
                    .oldValueJson(oldJson)
                    .newValueJson(newJson)
                    .ipAddress(ipAddress)
                    .timestamp(LocalDateTime.now())
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Logged audit action {} for assessment {}", action, assessmentId);
        } catch (Exception ex) {
            log.error("Failed to persist audit log for assessment {}: {}", assessmentId, ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AssessmentAuditLogResponse> getAuditLogsByAssessment(Long assessmentId, Pageable pageable) {
        Page<AssessmentAuditLog> page = auditLogRepository.findByAssessmentId(assessmentId, pageable);
        return PagedResponse.<AssessmentAuditLogResponse>builder()
                .content(auditLogMapper.toResponseList(page.getContent()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLast(page.isLast())
                .build();
    }
}
