package com.smartassessment.assessment.service.impl;

import com.smartassessment.assessment.client.IdentityServiceClient;
import com.smartassessment.assessment.client.NotificationServiceClient;
import com.smartassessment.assessment.dto.request.BulkEnrollCandidatesRequest;
import com.smartassessment.assessment.dto.request.EnrollCandidateRequest;
import com.smartassessment.assessment.dto.response.EnrollmentResponse;
import com.smartassessment.assessment.dto.response.PagedResponse;
import com.smartassessment.assessment.entity.Assessment;
import com.smartassessment.assessment.entity.AssessmentEnrollment;
import com.smartassessment.assessment.entity.AssessmentStatus;
import com.smartassessment.assessment.entity.AuditAction;
import com.smartassessment.assessment.entity.EnrollmentStatus;
import com.smartassessment.assessment.exception.BusinessException;
import com.smartassessment.assessment.exception.DuplicateResourceException;
import com.smartassessment.assessment.exception.ResourceNotFoundException;
import com.smartassessment.assessment.mapper.EnrollmentMapper;
import com.smartassessment.assessment.repository.AssessmentEnrollmentRepository;
import com.smartassessment.assessment.repository.AssessmentRepository;
import com.smartassessment.assessment.service.AuditLogService;
import com.smartassessment.assessment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentEnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final IdentityServiceClient identityServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public EnrollmentResponse enrollCandidate(Long assessmentId, EnrollCandidateRequest request, String userId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (assessment.getStatus() == AssessmentStatus.ARCHIVED || assessment.getStatus() == AssessmentStatus.CLOSED) {
            throw new BusinessException("Cannot enroll candidates in an archived or closed assessment");
        }

        if (enrollmentRepository.existsByAssessmentIdAndCandidateId(assessmentId, request.getCandidateId())) {
            throw new DuplicateResourceException(String.format(
                    "Candidate '%s' is already enrolled in assessment id '%d'", request.getCandidateId(), assessmentId
            ));
        }

        // Optional identity service candidate check
        identityServiceClient.validateCandidateActive(request.getCandidateId());

        String invitationToken = "inv-" + UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expiresAt = request.getExpiresAt() != null
                ? request.getExpiresAt()
                : (assessment.getValidTo() != null ? assessment.getValidTo() : LocalDateTime.now().plusMonths(1));

        AssessmentEnrollment enrollment = AssessmentEnrollment.builder()
                .assessment(assessment)
                .candidateId(request.getCandidateId())
                .candidateEmail(request.getCandidateEmail())
                .candidateName(request.getCandidateName())
                .enrollmentStatus(EnrollmentStatus.ENROLLED)
                .invitationToken(invitationToken)
                .scheduledStartTime(request.getScheduledStartTime() != null ? request.getScheduledStartTime() : assessment.getValidFrom())
                .scheduledEndTime(request.getScheduledEndTime() != null ? request.getScheduledEndTime() : assessment.getValidTo())
                .attemptCount(0)
                .enrolledBy(userId != null ? userId : "ADMIN")
                .enrolledAt(LocalDateTime.now())
                .expiresAt(expiresAt)
                .build();

        AssessmentEnrollment saved = enrollmentRepository.save(enrollment);

        // Trigger invitation email asynchronously
        notificationServiceClient.sendCandidateInvitation(
                saved.getCandidateEmail(),
                saved.getCandidateName(),
                assessment.getTitle(),
                saved.getInvitationToken(),
                assessment.getPasscode()
        );

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.CANDIDATE_ENROLLED,
                String.format("Enrolled candidate '%s' (%s)", saved.getCandidateName(), saved.getCandidateEmail()),
                null,
                enrollmentMapper.toResponse(saved),
                null
        );

        log.info("Candidate {} successfully enrolled in assessment {}", saved.getCandidateId(), assessmentId);
        return enrollmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public List<EnrollmentResponse> bulkEnrollCandidates(Long assessmentId, BulkEnrollCandidatesRequest request, String userId) {
        Assessment assessment = assessmentRepository.findByIdAndIsDeletedFalse(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + assessmentId));

        if (assessment.getStatus() == AssessmentStatus.ARCHIVED || assessment.getStatus() == AssessmentStatus.CLOSED) {
            throw new BusinessException("Cannot enroll candidates in an archived or closed assessment");
        }

        List<EnrollmentResponse> results = new ArrayList<>();
        for (EnrollCandidateRequest candidateReq : request.getCandidates()) {
            if (enrollmentRepository.existsByAssessmentIdAndCandidateId(assessmentId, candidateReq.getCandidateId())) {
                log.warn("Candidate {} already enrolled in assessment {}, skipping.", candidateReq.getCandidateId(), assessmentId);
                continue;
            }

            String invitationToken = "inv-" + UUID.randomUUID().toString().replace("-", "");
            LocalDateTime expiresAt = candidateReq.getExpiresAt() != null
                    ? candidateReq.getExpiresAt()
                    : (assessment.getValidTo() != null ? assessment.getValidTo() : LocalDateTime.now().plusMonths(1));

            AssessmentEnrollment enrollment = AssessmentEnrollment.builder()
                    .assessment(assessment)
                    .candidateId(candidateReq.getCandidateId())
                    .candidateEmail(candidateReq.getCandidateEmail())
                    .candidateName(candidateReq.getCandidateName())
                    .enrollmentStatus(EnrollmentStatus.ENROLLED)
                    .invitationToken(invitationToken)
                    .scheduledStartTime(candidateReq.getScheduledStartTime() != null ? candidateReq.getScheduledStartTime() : assessment.getValidFrom())
                    .scheduledEndTime(candidateReq.getScheduledEndTime() != null ? candidateReq.getScheduledEndTime() : assessment.getValidTo())
                    .attemptCount(0)
                    .enrolledBy(userId != null ? userId : "ADMIN")
                    .enrolledAt(LocalDateTime.now())
                    .expiresAt(expiresAt)
                    .build();

            AssessmentEnrollment saved = enrollmentRepository.save(enrollment);

            if (Boolean.TRUE.equals(request.getSendNotificationEmails())) {
                notificationServiceClient.sendCandidateInvitation(
                        saved.getCandidateEmail(),
                        saved.getCandidateName(),
                        assessment.getTitle(),
                        saved.getInvitationToken(),
                        assessment.getPasscode()
                );
            }

            results.add(enrollmentMapper.toResponse(saved));
        }

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.CANDIDATE_ENROLLED,
                String.format("Bulk enrolled %d candidates into assessment", results.size()),
                null,
                "Enrolled count: " + results.size(),
                null
        );

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EnrollmentResponse> getEnrollmentsByAssessment(
            Long assessmentId, EnrollmentStatus status, Pageable pageable) {
        if (!assessmentRepository.existsById(assessmentId)) {
            throw new ResourceNotFoundException("Assessment not found with id: " + assessmentId);
        }

        Page<AssessmentEnrollment> page = status != null
                ? enrollmentRepository.findByAssessmentIdAndEnrollmentStatus(assessmentId, status, pageable)
                : enrollmentRepository.findByAssessmentId(assessmentId, pageable);

        return PagedResponse.<EnrollmentResponse>builder()
                .content(enrollmentMapper.toResponseList(page.getContent()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLast(page.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentByToken(String token) {
        AssessmentEnrollment enrollment = enrollmentRepository.findByInvitationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or non-existent assessment invitation token"));
        return enrollmentMapper.toResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByCandidateId(String candidateId) {
        List<AssessmentEnrollment> enrollments = enrollmentRepository.findByCandidateId(candidateId);
        return enrollmentMapper.toResponseList(enrollments);
    }

    @Override
    @Transactional
    public EnrollmentResponse updateEnrollmentStatus(Long assessmentId, Long enrollmentId, EnrollmentStatus newStatus, String userId) {
        AssessmentEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));

        if (!enrollment.getAssessment().getId().equals(assessmentId)) {
            throw new BusinessException("Enrollment id does not belong to specified assessment id");
        }

        EnrollmentStatus oldStatus = enrollment.getEnrollmentStatus();
        enrollment.setEnrollmentStatus(newStatus);
        AssessmentEnrollment updated = enrollmentRepository.save(enrollment);

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.STATUS_CHANGED,
                String.format("Candidate %s enrollment status changed from %s to %s", enrollment.getCandidateId(), oldStatus, newStatus),
                oldStatus.name(),
                newStatus.name(),
                null
        );

        return enrollmentMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void cancelEnrollment(Long assessmentId, Long enrollmentId, String userId) {
        AssessmentEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));

        if (!enrollment.getAssessment().getId().equals(assessmentId)) {
            throw new BusinessException("Enrollment id does not belong to specified assessment id");
        }

        enrollment.setEnrollmentStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);

        auditLogService.logAction(
                assessmentId,
                userId,
                "ADMIN",
                AuditAction.STATUS_CHANGED,
                "Cancelled enrollment for candidate: " + enrollment.getCandidateEmail(),
                null,
                "CANCELLED",
                null
        );
    }
}
