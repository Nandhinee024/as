package com.smartassessment.assessment.mapper;

import com.smartassessment.assessment.dto.response.ProctoringRuleResponse;
import com.smartassessment.assessment.entity.AssessmentProctoringRule;
import org.springframework.stereotype.Component;

@Component
public class ProctoringRuleMapper {

    public ProctoringRuleResponse toResponse(AssessmentProctoringRule rule) {
        if (rule == null) {
            return null;
        }
        return ProctoringRuleResponse.builder()
                .id(rule.getId())
                .assessmentId(rule.getAssessment() != null ? rule.getAssessment().getId() : null)
                .cameraRequired(rule.getCameraRequired())
                .micRequired(rule.getMicRequired())
                .fullscreenEnforced(rule.getFullscreenEnforced())
                .tabSwitchLimit(rule.getTabSwitchLimit())
                .faceDetectionRequired(rule.getFaceDetectionRequired())
                .multipleFacesForbidden(rule.getMultipleFacesForbidden())
                .phoneDetectionForbidden(rule.getPhoneDetectionForbidden())
                .copyPasteForbidden(rule.getCopyPasteForbidden())
                .codeSimilarityCheck(rule.getCodeSimilarityCheck())
                .screenShareRequired(rule.getScreenShareRequired())
                .autoTerminateOnViolations(rule.getAutoTerminateOnViolations())
                .warningThreshold(rule.getWarningThreshold())
                .createdAt(rule.getCreatedAt())
                .updatedAt(rule.getUpdatedAt())
                .build();
    }
}
