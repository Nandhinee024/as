package com.smartassessment.assessment.service;

import com.smartassessment.assessment.dto.request.UpdateProctoringRulesRequest;
import com.smartassessment.assessment.dto.response.ProctoringRuleResponse;

public interface ProctoringRuleService {

    ProctoringRuleResponse getProctoringRulesByAssessmentId(Long assessmentId);

    ProctoringRuleResponse updateProctoringRules(Long assessmentId, UpdateProctoringRulesRequest request, String userId);
}
