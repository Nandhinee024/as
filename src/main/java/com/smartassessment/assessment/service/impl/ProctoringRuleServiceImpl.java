package com.smartassessment.assessment.service.impl;

import com.smartassessment.assessment.dto.request.UpdateProctoringRulesRequest;
import com.smartassessment.assessment.dto.response.ProctoringRuleResponse;
import com.smartassessment.assessment.entity.Assessment;
import com.smartassessment.assessment.entity.AssessmentProctoringRule;
import com.smartassessment.assessment.entity.AssessmentStatus;
import com.smartassessment.assessment.entity.AuditAction;
import com.smartassessment.assessment.exception.BusinessException;
import com.smartassessment.assessment.exception.ResourceNotFoundException;
import com.smartassessment.assessment.mapper.ProctoringRuleMapper;
import com.smartassessment.assessment.repository.AssessmentProctoringRuleRepository;
import com.smartassessment.assessment.repository.AssessmentRepository;
import com.smartassessment.assessment.service.AuditLogService;
import com.smartassessment.assessment.service.ProctoringRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProctoringRuleServiceImpl implements ProctoringRuleService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentProctoringRuleRepository proctoringRuleRepository;
    private final ProctoringRuleMapper proctoringRuleMapper;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public ProctoringRuleResponse getProctoringRulesByAssessmentId(Long assessmentId) {
        AssessmentProctoringRule rule = proctoringRuleRepository.findByAssessmentId(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Proctoring rules not configured for assessment id: " + assessmentId));
        return proctoringRuleMapper.toResponse(rule);
    }

    @Override
    @Transactional
    public ProctoringRuleResponse updateProctoringRules(Long assessmentId, UpdateProctoringRulesRequest request, String userId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (assessment.getStatus() == AssessmentStatus.CLOSED || assessment.getStatus() == AssessmentStatus.ARCHIVED) {
            throw new BusinessException("Cannot modify proctoring rules of an archived or closed assessment");
        }

        AssessmentProctoringRule rule = proctoringRuleRepository.findByAssessmentId(assessmentId)
                .orElseGet(() -> AssessmentProctoringRule.builder().assessment(assessment).build());

        ProctoringRuleResponse oldState = proctoringRuleMapper.toResponse(rule);

        rule.setCameraRequired(request.getCameraRequired());
        rule.setMicRequired(request.getMicRequired());
        rule.setFullscreenEnforced(request.getFullscreenEnforced());
        rule.setTabSwitchLimit(request.getTabSwitchLimit());
        rule.setFaceDetectionRequired(request.getFaceDetectionRequired());
        rule.setMultipleFacesForbidden(request.getMultipleFacesForbidden());
        rule.setPhoneDetectionForbidden(request.getPhoneDetectionForbidden());
        rule.setCopyPasteForbidden(request.getCopyPasteForbidden());
        rule.setCodeSimilarityCheck(request.getCodeSimilarityCheck());
        rule.setScreenShareRequired(request.getScreenShareRequired());
        rule.setAutoTerminateOnViolations(request.getAutoTerminateOnViolations());
        rule.setWarningThreshold(request.getWarningThreshold());

        AssessmentProctoringRule savedRule = proctoringRuleRepository.save(rule);

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.RULES_UPDATED,
                "Updated proctoring rules for assessment: " + assessment.getTitle(),
                oldState,
                proctoringRuleMapper.toResponse(savedRule),
                null
        );

        log.info("Proctoring rules updated for assessment id: {} by user: {}", assessmentId, userId);
        return proctoringRuleMapper.toResponse(savedRule);
    }
}
