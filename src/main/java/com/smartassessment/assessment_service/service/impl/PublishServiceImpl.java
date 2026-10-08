package com.smartassessment.assessment_service.service.impl;

import com.smartassessment.assessment_service.dto.response.AssessmentResponse;
import com.smartassessment.assessment_service.entity.Assessment;
import com.smartassessment.assessment_service.enums.AssessmentStatus;
import com.smartassessment.assessment_service.exception.InvalidAssessmentException;
import com.smartassessment.assessment_service.exception.ResourceNotFoundException;
import com.smartassessment.assessment_service.repository.AssessmentRepository;
import com.smartassessment.assessment_service.service.AssessmentService;
import com.smartassessment.assessment_service.service.PublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishServiceImpl implements PublishService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentService assessmentService;

    @Override
    @Transactional
    public AssessmentResponse publishAssessment(Long assessmentId) {
        log.info("Publishing assessment with id: {}", assessmentId);

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (assessment.getStatus() == AssessmentStatus.PUBLISHED) {
            throw new InvalidAssessmentException("Assessment is already published.");
        }

        if (assessment.getStatus() == AssessmentStatus.ARCHIVED) {
            throw new InvalidAssessmentException("Archived assessment cannot be published.");
        }

        if (assessment.getSections() == null || assessment.getSections().isEmpty()) {
            throw new InvalidAssessmentException("Assessment must have at least one section before publishing.");
        }

        assessment.setStatus(AssessmentStatus.PUBLISHED);
        assessmentRepository.save(assessment);
        log.info("Assessment published successfully with id: {}", assessmentId);
        return assessmentService.getAssessmentById(assessmentId);
    }

    @Override
    @Transactional
    public AssessmentResponse archiveAssessment(Long assessmentId) {
        log.info("Archiving assessment with id: {}", assessmentId);

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (assessment.getStatus() == AssessmentStatus.ARCHIVED) {
            throw new InvalidAssessmentException("Assessment is already archived.");
        }

        assessment.setStatus(AssessmentStatus.ARCHIVED);
        assessmentRepository.save(assessment);
        log.info("Assessment archived successfully with id: {}", assessmentId);
        return assessmentService.getAssessmentById(assessmentId);
    }
}
