package com.smartassessment.assessment.service.impl;

import com.smartassessment.assessment.dto.request.AssessmentSearchCriteria;
import com.smartassessment.assessment.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment.dto.request.PublishAssessmentRequest;
import com.smartassessment.assessment.dto.request.UpdateAssessmentRequest;
import com.smartassessment.assessment.dto.response.*;
import com.smartassessment.assessment.entity.*;
import com.smartassessment.assessment.exception.BusinessException;
import com.smartassessment.assessment.exception.DuplicateResourceException;
import com.smartassessment.assessment.exception.ResourceNotFoundException;
import com.smartassessment.assessment.mapper.AssessmentMapper;
import com.smartassessment.assessment.repository.AssessmentEnrollmentRepository;
import com.smartassessment.assessment.repository.AssessmentProctoringRuleRepository;
import com.smartassessment.assessment.repository.AssessmentRepository;
import com.smartassessment.assessment.repository.AssessmentSectionRepository;
import com.smartassessment.assessment.service.AssessmentService;
import com.smartassessment.assessment.service.AuditLogService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentSectionRepository sectionRepository;
    private final AssessmentProctoringRuleRepository proctoringRuleRepository;
    private final AssessmentEnrollmentRepository enrollmentRepository;
    private final AssessmentMapper assessmentMapper;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AssessmentDetailResponse createAssessment(CreateAssessmentRequest request, String userId) {
        log.info("Creating new assessment with code: {} by user: {}", request.getCode(), userId);

        String normalizedCode = request.getCode().toUpperCase().trim();
        if (assessmentRepository.existsByCodeAndIsDeletedFalse(normalizedCode)) {
            throw new DuplicateResourceException("Assessment code already exists: " + normalizedCode);
        }

        if (request.getPassingMarks().compareTo(request.getTotalMarks()) > 0) {
            throw new BusinessException("Passing marks cannot exceed total marks");
        }

        Assessment assessment = assessmentMapper.toEntity(request, userId);

        // Encode passcode if provided and protected
        if (request.getAccessType() == AccessType.PASSWORD_PROTECTED && StringUtils.hasText(request.getPasscode())) {
            assessment.setPasscode(passwordEncoder.encode(request.getPasscode()));
        }

        Assessment savedAssessment = assessmentRepository.save(assessment);

        // Initialize default proctoring rules
        AssessmentProctoringRule defaultRules = AssessmentProctoringRule.builder()
                .assessment(savedAssessment)
                .cameraRequired(true)
                .micRequired(true)
                .fullscreenEnforced(true)
                .tabSwitchLimit(3)
                .faceDetectionRequired(true)
                .multipleFacesForbidden(true)
                .phoneDetectionForbidden(true)
                .copyPasteForbidden(true)
                .codeSimilarityCheck(true)
                .screenShareRequired(false)
                .autoTerminateOnViolations(true)
                .warningThreshold(3)
                .build();
        proctoringRuleRepository.save(defaultRules);
        savedAssessment.setProctoringRule(defaultRules);

        auditLogService.logAction(
                savedAssessment.getId(),
                userId,
                "ADMIN",
                AuditAction.CREATED,
                "Created assessment: " + savedAssessment.getTitle(),
                null,
                assessmentMapper.toDetailResponse(savedAssessment),
                null
        );

        log.info("Assessment successfully created with ID: {}", savedAssessment.getId());
        return assessmentMapper.toDetailResponse(savedAssessment);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentDetailResponse getAssessmentById(Long id) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));
        return assessmentMapper.toDetailResponse(assessment);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentDetailResponse getAssessmentByCode(String code) {
        Assessment assessment = assessmentRepository.findByCodeAndIsDeletedFalse(code.toUpperCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with code: " + code));
        return assessmentMapper.toDetailResponse(assessment);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AssessmentSummaryResponse> searchAssessments(AssessmentSearchCriteria criteria, Pageable pageable) {
        Specification<Assessment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (criteria != null) {
                if (StringUtils.hasText(criteria.getQuery())) {
                    String pattern = "%" + criteria.getQuery().toLowerCase() + "%";
                    Predicate titlePred = cb.like(cb.lower(root.get("title")), pattern);
                    Predicate codePred = cb.like(cb.lower(root.get("code")), pattern);
                    Predicate descPred = cb.like(cb.lower(root.get("description")), pattern);
                    predicates.add(cb.or(titlePred, codePred, descPred));
                }
                if (criteria.getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
                }
                if (StringUtils.hasText(criteria.getCategory())) {
                    predicates.add(cb.equal(root.get("category"), criteria.getCategory()));
                }
                if (criteria.getAccessType() != null) {
                    predicates.add(cb.equal(root.get("accessType"), criteria.getAccessType()));
                }
                if (StringUtils.hasText(criteria.getCreatedBy())) {
                    predicates.add(cb.equal(root.get("createdBy"), criteria.getCreatedBy()));
                }
                if (criteria.getValidFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("validFrom"), criteria.getValidFrom()));
                }
                if (criteria.getValidTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("validTo"), criteria.getValidTo()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Assessment> page = assessmentRepository.findAll(spec, pageable);
        return PagedResponse.<AssessmentSummaryResponse>builder()
                .content(assessmentMapper.toSummaryResponseList(page.getContent()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLast(page.isLast())
                .build();
    }

    @Override
    @Transactional
    public AssessmentDetailResponse updateAssessment(Long id, UpdateAssessmentRequest request, String userId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));

        if (assessment.getStatus() == AssessmentStatus.CLOSED) {
            throw new BusinessException("Cannot modify an assessment that has been marked CLOSED");
        }

        if (request.getPassingMarks().compareTo(request.getTotalMarks()) > 0) {
            throw new BusinessException("Passing marks cannot exceed total marks");
        }

        AssessmentDetailResponse oldState = assessmentMapper.toDetailResponse(assessment);

        BigDecimal passingPercentage = request.getPassingPercentage();
        if (passingPercentage == null && request.getTotalMarks() != null && request.getPassingMarks() != null
                && request.getTotalMarks().compareTo(BigDecimal.ZERO) > 0) {
            passingPercentage = request.getPassingMarks()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(request.getTotalMarks(), 2, RoundingMode.HALF_UP);
        }

        assessment.setTitle(request.getTitle());
        assessment.setDescription(request.getDescription());
        assessment.setInstructions(request.getInstructions());
        assessment.setCategory(request.getCategory());
        if (request.getStatus() != null) {
            assessment.setStatus(request.getStatus());
        }
        if (request.getAccessType() != null) {
            assessment.setAccessType(request.getAccessType());
        }
        if (request.getAccessType() == AccessType.PASSWORD_PROTECTED && StringUtils.hasText(request.getPasscode())) {
            assessment.setPasscode(passwordEncoder.encode(request.getPasscode()));
        }
        assessment.setTotalDurationMinutes(request.getTotalDurationMinutes());
        assessment.setTotalMarks(request.getTotalMarks());
        assessment.setPassingMarks(request.getPassingMarks());
        assessment.setPassingPercentage(passingPercentage != null ? passingPercentage : assessment.getPassingPercentage());
        assessment.setValidFrom(request.getValidFrom());
        assessment.setValidTo(request.getValidTo());
        if (request.getShuffleQuestions() != null) assessment.setShuffleQuestions(request.getShuffleQuestions());
        if (request.getShuffleOptions() != null) assessment.setShuffleOptions(request.getShuffleOptions());
        if (request.getAllowNavigationBack() != null) assessment.setAllowNavigationBack(request.getAllowNavigationBack());
        if (request.getAllowReview() != null) assessment.setAllowReview(request.getAllowReview());
        if (request.getAllowRetake() != null) assessment.setAllowRetake(request.getAllowRetake());
        if (request.getMaxRetakeAttempts() != null) assessment.setMaxRetakeAttempts(request.getMaxRetakeAttempts());
        if (request.getAutoSubmitOnExpiry() != null) assessment.setAutoSubmitOnExpiry(request.getAutoSubmitOnExpiry());
        if (request.getConnectionGracePeriodMinutes() != null) assessment.setConnectionGracePeriodMinutes(request.getConnectionGracePeriodMinutes());
        if (request.getDynamicQuestionnaireEnabled() != null) assessment.setDynamicQuestionnaireEnabled(request.getDynamicQuestionnaireEnabled());
        if (request.getShowResultsImmediately() != null) assessment.setShowResultsImmediately(request.getShowResultsImmediately());
        assessment.setUpdatedBy(userId);

        Assessment updatedAssessment = assessmentRepository.save(assessment);

        auditLogService.logAction(
                id,
                userId,
                "ADMIN",
                AuditAction.UPDATED,
                "Updated assessment details: " + updatedAssessment.getTitle(),
                oldState,
                assessmentMapper.toDetailResponse(updatedAssessment),
                null
        );

        log.info("Assessment {} updated by user {}", id, userId);
        return assessmentMapper.toDetailResponse(updatedAssessment);
    }

    @Override
    @Transactional
    public void deleteAssessment(Long id, String userId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));

        AssessmentDetailResponse oldState = assessmentMapper.toDetailResponse(assessment);

        assessment.setIsDeleted(true);
        assessment.setDeletedAt(LocalDateTime.now());
        assessment.setStatus(AssessmentStatus.ARCHIVED);
        assessment.setUpdatedBy(userId);
        assessmentRepository.save(assessment);

        auditLogService.logAction(
                id,
                userId,
                "ADMIN",
                AuditAction.ARCHIVED,
                "Soft deleted/archived assessment: " + assessment.getTitle(),
                oldState,
                null,
                null
        );

        log.info("Assessment {} soft deleted by {}", id, userId);
    }

    @Override
    @Transactional
    public AssessmentDetailResponse publishAssessment(Long id, PublishAssessmentRequest request, String userId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));

        if (assessment.getStatus() == AssessmentStatus.PUBLISHED || assessment.getStatus() == AssessmentStatus.ACTIVE) {
            throw new BusinessException("Assessment is already published");
        }

        // Rule: Assessment must contain at least 1 section
        List<AssessmentSection> sections = sectionRepository.findByAssessmentIdOrderBySectionOrderAsc(id);
        if (sections.isEmpty()) {
            throw new BusinessException("Cannot publish assessment without any configured sections");
        }

        // Rule: Check that each section has questions configured
        int totalQuestions = 0;
        for (AssessmentSection section : sections) {
            if (section.getTotalQuestionsToAsk() == null || section.getTotalQuestionsToAsk() <= 0) {
                throw new BusinessException("Section '" + section.getTitle() + "' has no questions configured");
            }
            totalQuestions += section.getTotalQuestionsToAsk();
        }

        if (totalQuestions == 0) {
            throw new BusinessException("Cannot publish assessment: total questions count across sections must be greater than 0");
        }

        if (request != null) {
            if (request.getValidFrom() != null) {
                assessment.setValidFrom(request.getValidFrom());
            }
            if (request.getValidTo() != null) {
                assessment.setValidTo(request.getValidTo());
            }
        }

        AssessmentStatus oldStatus = assessment.getStatus();
        assessment.setStatus(AssessmentStatus.PUBLISHED);
        assessment.setUpdatedBy(userId);
        Assessment saved = assessmentRepository.save(assessment);

        auditLogService.logAction(
                id,
                userId,
                "ADMIN",
                AuditAction.PUBLISHED,
                "Published assessment: " + assessment.getTitle(),
                oldStatus.name(),
                AssessmentStatus.PUBLISHED.name(),
                null
        );

        log.info("Assessment {} published by user {}", id, userId);
        return assessmentMapper.toDetailResponse(saved);
    }

    @Override
    @Transactional
    public AssessmentDetailResponse archiveAssessment(Long id, String userId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));

        AssessmentStatus oldStatus = assessment.getStatus();
        assessment.setStatus(AssessmentStatus.ARCHIVED);
        assessment.setUpdatedBy(userId);
        Assessment saved = assessmentRepository.save(assessment);

        auditLogService.logAction(
                id,
                userId,
                "ADMIN",
                AuditAction.ARCHIVED,
                "Archived assessment: " + assessment.getTitle(),
                oldStatus.name(),
                AssessmentStatus.ARCHIVED.name(),
                null
        );

        return assessmentMapper.toDetailResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateAssessmentAccessResponse verifyCandidateAccessByToken(String invitationToken) {
        AssessmentEnrollment enrollment = enrollmentRepository.findByInvitationToken(invitationToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or non-existent assessment invitation token"));

        Assessment assessment = enrollment.getAssessment();
        if (assessment == null || Boolean.TRUE.equals(assessment.getIsDeleted())) {
            throw new ResourceNotFoundException("Associated assessment is no longer available");
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isEligible = true;
        String message = "Candidate is eligible to begin assessment";

        if (assessment.getStatus() == AssessmentStatus.DRAFT) {
            isEligible = false;
            message = "Assessment is currently in draft mode and not yet open.";
        } else if (assessment.getStatus() == AssessmentStatus.ARCHIVED || assessment.getStatus() == AssessmentStatus.CLOSED) {
            isEligible = false;
            message = "Assessment has closed or is no longer active.";
        } else if (enrollment.getEnrollmentStatus() == EnrollmentStatus.COMPLETED) {
            isEligible = false;
            message = "Assessment has already been completed.";
        } else if (enrollment.getEnrollmentStatus() == EnrollmentStatus.CANCELLED) {
            isEligible = false;
            message = "Enrollment has been cancelled by an administrator.";
        } else if (enrollment.getExpiresAt() != null && now.isAfter(enrollment.getExpiresAt())) {
            isEligible = false;
            message = "Your invitation link has expired.";
        } else if (enrollment.getScheduledStartTime() != null && now.isBefore(enrollment.getScheduledStartTime())) {
            isEligible = false;
            message = "Assessment window has not yet opened. Scheduled start: " + enrollment.getScheduledStartTime();
        } else if (enrollment.getScheduledEndTime() != null && now.isAfter(enrollment.getScheduledEndTime())) {
            isEligible = false;
            message = "Assessment window has closed. Scheduled end: " + enrollment.getScheduledEndTime();
        }

        List<CandidateAssessmentAccessResponse.SectionOverview> sectionOverviews = new ArrayList<>();
        if (assessment.getSections() != null) {
            for (AssessmentSection sec : assessment.getSections()) {
                sectionOverviews.add(CandidateAssessmentAccessResponse.SectionOverview.builder()
                        .sectionId(sec.getId())
                        .title(sec.getTitle())
                        .sectionOrder(sec.getSectionOrder())
                        .totalQuestions(sec.getTotalQuestionsToAsk())
                        .questionType(sec.getQuestionType().name())
                        .sectionDurationMinutes(sec.getSectionDurationMinutes())
                        .build());
            }
        }

        AssessmentProctoringRule proctoring = assessment.getProctoringRule();
        CandidateAssessmentAccessResponse.ProctoringRequirements reqs = CandidateAssessmentAccessResponse.ProctoringRequirements.builder()
                .cameraRequired(proctoring != null ? proctoring.getCameraRequired() : true)
                .micRequired(proctoring != null ? proctoring.getMicRequired() : true)
                .fullscreenEnforced(proctoring != null ? proctoring.getFullscreenEnforced() : true)
                .faceDetectionRequired(proctoring != null ? proctoring.getFaceDetectionRequired() : true)
                .copyPasteForbidden(proctoring != null ? proctoring.getCopyPasteForbidden() : true)
                .tabSwitchLimit(proctoring != null ? proctoring.getTabSwitchLimit() : 3)
                .build();

        return CandidateAssessmentAccessResponse.builder()
                .assessmentId(assessment.getId())
                .title(assessment.getTitle())
                .code(assessment.getCode())
                .description(assessment.getDescription())
                .instructions(assessment.getInstructions())
                .category(assessment.getCategory())
                .status(assessment.getStatus())
                .accessType(assessment.getAccessType())
                .totalDurationMinutes(assessment.getTotalDurationMinutes())
                .totalMarks(assessment.getTotalMarks())
                .passingMarks(assessment.getPassingMarks())
                .candidateId(enrollment.getCandidateId())
                .candidateEmail(enrollment.getCandidateEmail())
                .candidateName(enrollment.getCandidateName())
                .invitationToken(enrollment.getInvitationToken())
                .scheduledStartTime(enrollment.getScheduledStartTime())
                .scheduledEndTime(enrollment.getScheduledEndTime())
                .passcodeRequired(assessment.getAccessType() == AccessType.PASSWORD_PROTECTED)
                .isEligibleToStart(isEligible)
                .eligibilityMessage(message)
                .sectionOverviews(sectionOverviews)
                .proctoringRequirements(reqs)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyAssessmentPasscode(Long assessmentId, String candidateId, String passcode) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (assessment.getAccessType() != AccessType.PASSWORD_PROTECTED || !StringUtils.hasText(assessment.getPasscode())) {
            return true;
        }

        if (!StringUtils.hasText(passcode)) {
            return false;
        }

        return passwordEncoder.matches(passcode, assessment.getPasscode());
    }

    @Override
    @Transactional(readOnly = true)
    public ExamSessionAssessmentConfigResponse getAssessmentConfigForExamSession(Long assessmentId, String candidateId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (StringUtils.hasText(candidateId)) {
            boolean enrolled = enrollmentRepository.existsByAssessmentIdAndCandidateId(assessmentId, candidateId);
            if (!enrolled && assessment.getAccessType() == AccessType.INVITE_ONLY) {
                throw new BusinessException("Candidate " + candidateId + " is not enrolled in this private assessment");
            }
        }

        return assessmentMapper.toExamSessionConfig(assessment);
    }
}
