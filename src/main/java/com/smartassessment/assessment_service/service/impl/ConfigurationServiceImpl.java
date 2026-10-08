package com.smartassessment.assessment_service.service.impl;

import com.smartassessment.assessment_service.dto.request.ConfigurationRequest;
import com.smartassessment.assessment_service.dto.request.PassingCriteriaRequest;
import com.smartassessment.assessment_service.dto.request.ProctoringRuleRequest;
import com.smartassessment.assessment_service.dto.response.ExamConfigResponse;
import com.smartassessment.assessment_service.entity.Assessment;
import com.smartassessment.assessment_service.entity.PassingCriteria;
import com.smartassessment.assessment_service.entity.ProctoringRule;
import com.smartassessment.assessment_service.exception.ResourceNotFoundException;
import com.smartassessment.assessment_service.repository.AssessmentRepository;
import com.smartassessment.assessment_service.repository.PassingCriteriaRepository;
import com.smartassessment.assessment_service.repository.ProctoringRuleRepository;
import com.smartassessment.assessment_service.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigurationServiceImpl implements ConfigurationService {

    private final AssessmentRepository assessmentRepository;
    private final PassingCriteriaRepository passingCriteriaRepository;
    private final ProctoringRuleRepository proctoringRuleRepository;

    @Override
    @Transactional
    public ExamConfigResponse saveConfiguration(ConfigurationRequest request) {
        log.info("Saving configuration for assessment id: {}", request.getAssessmentId());

        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + request.getAssessmentId()));

        // Save Passing Criteria
        if (request.getPassingCriteria() != null) {
            PassingCriteriaRequest pcReq = request.getPassingCriteria();
            PassingCriteria criteria = passingCriteriaRepository
                    .findByAssessmentId(assessment.getId())
                    .orElse(new PassingCriteria());
            criteria.setAssessment(assessment);
            criteria.setPassingPercentage(pcReq.getPassingPercentage());
            criteria.setPassingMarks(pcReq.getPassingMarks());
            passingCriteriaRepository.save(criteria);
            log.info("Passing criteria saved for assessment id: {}", assessment.getId());
        }

        // Save Proctoring Rules
        if (request.getProctoringRule() != null) {
            ProctoringRuleRequest prReq = request.getProctoringRule();
            ProctoringRule rule = proctoringRuleRepository
                    .findByAssessmentId(assessment.getId())
                    .orElse(new ProctoringRule());
            rule.setAssessment(assessment);
            rule.setIsCameraRequired(prReq.getIsCameraRequired());
            rule.setIsScreenShare(prReq.getIsScreenShare());
            rule.setMaxTabSwitches(prReq.getMaxTabSwitches());
            proctoringRuleRepository.save(rule);
            log.info("Proctoring rules saved for assessment id: {}", assessment.getId());
        }

        return getConfiguration(assessment.getId());
    }

    @Override
    public ExamConfigResponse getConfiguration(Long assessmentId) {
        log.info("Fetching configuration for assessment id: {}", assessmentId);

        if (!assessmentRepository.existsById(assessmentId)) {
            throw new ResourceNotFoundException("Assessment not found with id: " + assessmentId);
        }

        ExamConfigResponse response = new ExamConfigResponse();
        response.setAssessmentId(assessmentId);

        // Load passing criteria
        passingCriteriaRepository.findByAssessmentId(assessmentId).ifPresent(criteria -> {
            response.setPassingPercentage(criteria.getPassingPercentage());
            response.setPassingMarks(criteria.getPassingMarks());
        });

        // Load proctoring rules
        proctoringRuleRepository.findByAssessmentId(assessmentId).ifPresent(rule -> {
            response.setIsCameraRequired(rule.getIsCameraRequired());
            response.setIsScreenShare(rule.getIsScreenShare());
            response.setMaxTabSwitches(rule.getMaxTabSwitches());
        });

        return response;
    }
}
