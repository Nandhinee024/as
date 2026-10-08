package com.smartassessment.assessment.service.impl;

import com.smartassessment.assessment.client.QuestionBankClient;
import com.smartassessment.assessment.dto.request.CreateSectionRequest;
import com.smartassessment.assessment.dto.request.UpdateSectionRequest;
import com.smartassessment.assessment.dto.response.SectionResponse;
import com.smartassessment.assessment.entity.Assessment;
import com.smartassessment.assessment.entity.AssessmentSection;
import com.smartassessment.assessment.entity.AssessmentStatus;
import com.smartassessment.assessment.entity.AuditAction;
import com.smartassessment.assessment.exception.BusinessException;
import com.smartassessment.assessment.exception.ResourceNotFoundException;
import com.smartassessment.assessment.mapper.SectionMapper;
import com.smartassessment.assessment.repository.AssessmentRepository;
import com.smartassessment.assessment.repository.AssessmentSectionRepository;
import com.smartassessment.assessment.service.AuditLogService;
import com.smartassessment.assessment.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentSectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final QuestionBankClient questionBankClient;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public SectionResponse addSection(Long assessmentId, CreateSectionRequest request, String userId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (assessment.getStatus() == AssessmentStatus.ARCHIVED || assessment.getStatus() == AssessmentStatus.CLOSED) {
            throw new BusinessException("Cannot add sections to an archived or closed assessment");
        }

        // Validate question distribution numbers
        int totalDifficultySum = (request.getEasyQuestionCount() != null ? request.getEasyQuestionCount() : 0)
                + (request.getMediumQuestionCount() != null ? request.getMediumQuestionCount() : 0)
                + (request.getHardQuestionCount() != null ? request.getHardQuestionCount() : 0);

        if (totalDifficultySum > 0 && totalDifficultySum != request.getTotalQuestionsToAsk()) {
            throw new BusinessException(String.format(
                    "Sum of difficulty questions (%d) does not match total questions to ask (%d)",
                    totalDifficultySum, request.getTotalQuestionsToAsk()
            ));
        }

        // Validate question set in question bank if provided
        if (request.getQuestionSetId() != null && !request.getQuestionSetId().isBlank()) {
            boolean valid = questionBankClient.validateQuestionSetExists(request.getQuestionSetId(), request.getTotalQuestionsToAsk());
            if (!valid) {
                log.warn("Question set {} validation warning from question bank", request.getQuestionSetId());
            }
        }

        int nextOrder = request.getSectionOrder() != null ? request.getSectionOrder() :
                sectionRepository.findMaxSectionOrderByAssessmentId(assessmentId) + 1;

        AssessmentSection section = AssessmentSection.builder()
                .assessment(assessment)
                .title(request.getTitle())
                .description(request.getDescription())
                .sectionOrder(nextOrder)
                .sectionDurationMinutes(request.getSectionDurationMinutes())
                .cutoffMarks(request.getCutoffMarks())
                .weightagePercentage(request.getWeightagePercentage() != null ? request.getWeightagePercentage() : BigDecimal.ZERO)
                .questionType(request.getQuestionType())
                .totalQuestionsToAsk(request.getTotalQuestionsToAsk())
                .easyQuestionCount(request.getEasyQuestionCount() != null ? request.getEasyQuestionCount() : 0)
                .mediumQuestionCount(request.getMediumQuestionCount() != null ? request.getMediumQuestionCount() : 0)
                .hardQuestionCount(request.getHardQuestionCount() != null ? request.getHardQuestionCount() : 0)
                .questionSetId(request.getQuestionSetId())
                .build();

        AssessmentSection savedSection = sectionRepository.save(section);

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.SECTION_ADDED,
                "Added section '" + section.getTitle() + "' to assessment",
                null,
                sectionMapper.toResponse(savedSection),
                null
        );

        log.info("Section {} added to assessment {}", savedSection.getId(), assessmentId);
        return sectionMapper.toResponse(savedSection);
    }

    @Override
    @Transactional
    public SectionResponse updateSection(Long assessmentId, Long sectionId, UpdateSectionRequest request, String userId) {
        AssessmentSection section = sectionRepository.findByIdAndAssessmentId(sectionId, assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + sectionId + " for assessment: " + assessmentId));

        if (section.getAssessment().getStatus() == AssessmentStatus.CLOSED) {
            throw new BusinessException("Cannot modify sections of a closed assessment");
        }

        SectionResponse oldState = sectionMapper.toResponse(section);

        section.setTitle(request.getTitle());
        section.setDescription(request.getDescription());
        if (request.getSectionOrder() != null) {
            section.setSectionOrder(request.getSectionOrder());
        }
        section.setSectionDurationMinutes(request.getSectionDurationMinutes());
        section.setCutoffMarks(request.getCutoffMarks());
        section.setWeightagePercentage(request.getWeightagePercentage());
        section.setQuestionType(request.getQuestionType());
        section.setTotalQuestionsToAsk(request.getTotalQuestionsToAsk());
        section.setEasyQuestionCount(request.getEasyQuestionCount() != null ? request.getEasyQuestionCount() : 0);
        section.setMediumQuestionCount(request.getMediumQuestionCount() != null ? request.getMediumQuestionCount() : 0);
        section.setHardQuestionCount(request.getHardQuestionCount() != null ? request.getHardQuestionCount() : 0);
        section.setQuestionSetId(request.getQuestionSetId());

        AssessmentSection updatedSection = sectionRepository.save(section);

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.SECTION_UPDATED,
                "Updated section '" + section.getTitle() + "'",
                oldState,
                sectionMapper.toResponse(updatedSection),
                null
        );

        return sectionMapper.toResponse(updatedSection);
    }

    @Override
    @Transactional
    public void deleteSection(Long assessmentId, Long sectionId, String userId) {
        AssessmentSection section = sectionRepository.findByIdAndAssessmentId(sectionId, assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + sectionId));

        SectionResponse oldState = sectionMapper.toResponse(section);
        sectionRepository.delete(section);

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.SECTION_DELETED,
                "Deleted section: " + section.getTitle(),
                oldState,
                null,
                null
        );

        log.info("Section {} deleted from assessment {}", sectionId, assessmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public SectionResponse getSectionById(Long assessmentId, Long sectionId) {
        AssessmentSection section = sectionRepository.findByIdAndAssessmentId(sectionId, assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + sectionId));
        return sectionMapper.toResponse(section);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionResponse> getSectionsByAssessmentId(Long assessmentId) {
        if (!assessmentRepository.existsById(assessmentId)) {
            throw new ResourceNotFoundException("Assessment not found with id: " + assessmentId);
        }
        List<AssessmentSection> sections = sectionRepository.findByAssessmentIdOrderBySectionOrderAsc(assessmentId);
        return sectionMapper.toResponseList(sections);
    }

    @Override
    @Transactional
    public List<SectionResponse> reorderSections(Long assessmentId, List<Long> sectionIds, String userId) {
        List<AssessmentSection> sections = sectionRepository.findByAssessmentIdOrderBySectionOrderAsc(assessmentId);
        Map<Long, AssessmentSection> sectionMap = sections.stream()
                .collect(Collectors.toMap(AssessmentSection::getId, s -> s));

        int order = 1;
        for (Long sId : sectionIds) {
            AssessmentSection section = sectionMap.get(sId);
            if (section != null) {
                section.setSectionOrder(order++);
                sectionRepository.save(section);
            }
        }

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.SECTION_UPDATED,
                "Reordered sections for assessment id: " + assessmentId,
                null,
                sectionIds,
                null
        );

        return getSectionsByAssessmentId(assessmentId);
    }
}
