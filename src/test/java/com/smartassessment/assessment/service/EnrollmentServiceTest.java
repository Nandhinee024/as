package com.smartassessment.assessment.service;

import com.smartassessment.assessment.client.IdentityServiceClient;
import com.smartassessment.assessment.client.NotificationServiceClient;
import com.smartassessment.assessment.dto.request.EnrollCandidateRequest;
import com.smartassessment.assessment.dto.response.EnrollmentResponse;
import com.smartassessment.assessment.entity.Assessment;
import com.smartassessment.assessment.entity.AssessmentEnrollment;
import com.smartassessment.assessment.entity.AssessmentStatus;
import com.smartassessment.assessment.entity.EnrollmentStatus;
import com.smartassessment.assessment.exception.DuplicateResourceException;
import com.smartassessment.assessment.mapper.EnrollmentMapper;
import com.smartassessment.assessment.repository.AssessmentEnrollmentRepository;
import com.smartassessment.assessment.repository.AssessmentRepository;
import com.smartassessment.assessment.service.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentEnrollmentRepository enrollmentRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @Mock
    private IdentityServiceClient identityServiceClient;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Assessment sampleAssessment;
    private AssessmentEnrollment sampleEnrollment;
    private EnrollmentResponse sampleEnrollmentResponse;

    @BeforeEach
    void setUp() {
        sampleAssessment = Assessment.builder()
                .id(1L)
                .title("Java Assessment")
                .status(AssessmentStatus.PUBLISHED)
                .isDeleted(false)
                .build();

        sampleEnrollment = AssessmentEnrollment.builder()
                .id(100L)
                .assessment(sampleAssessment)
                .candidateId("cand-1001")
                .candidateEmail("jane.doe@example.com")
                .candidateName("Jane Doe")
                .invitationToken("inv-token-12345")
                .enrollmentStatus(EnrollmentStatus.ENROLLED)
                .enrolledAt(LocalDateTime.now())
                .build();

        sampleEnrollmentResponse = EnrollmentResponse.builder()
                .id(100L)
                .assessmentId(1L)
                .candidateId("cand-1001")
                .candidateEmail("jane.doe@example.com")
                .invitationToken("inv-token-12345")
                .enrollmentStatus(EnrollmentStatus.ENROLLED)
                .build();
    }

    @Test
    @DisplayName("Should enroll candidate and dispatch invitation notification")
    void testEnrollCandidate_Success() {
        EnrollCandidateRequest request = EnrollCandidateRequest.builder()
                .candidateId("cand-1001")
                .candidateEmail("jane.doe@example.com")
                .candidateName("Jane Doe")
                .build();

        when(assessmentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(sampleAssessment));
        when(enrollmentRepository.existsByAssessmentIdAndCandidateId(1L, "cand-1001")).thenReturn(false);
        when(enrollmentRepository.save(any(AssessmentEnrollment.class))).thenReturn(sampleEnrollment);
        when(enrollmentMapper.toResponse(any(AssessmentEnrollment.class))).thenReturn(sampleEnrollmentResponse);

        EnrollmentResponse result = enrollmentService.enrollCandidate(1L, request, "admin-101");

        assertNotNull(result);
        assertEquals("cand-1001", result.getCandidateId());
        verify(notificationServiceClient, times(1)).sendCandidateInvitation(eq("jane.doe@example.com"), eq("Jane Doe"), any(), any(), any());
        verify(enrollmentRepository, times(1)).save(any(AssessmentEnrollment.class));
    }

    @Test
    @DisplayName("Should reject duplicate enrollment for the same candidate and assessment")
    void testEnrollCandidate_DuplicateCandidate() {
        EnrollCandidateRequest request = EnrollCandidateRequest.builder()
                .candidateId("cand-1001")
                .candidateEmail("jane.doe@example.com")
                .candidateName("Jane Doe")
                .build();

        when(assessmentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(sampleAssessment));
        when(enrollmentRepository.existsByAssessmentIdAndCandidateId(1L, "cand-1001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> enrollmentService.enrollCandidate(1L, request, "admin-101"));
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve enrollment by unique invitation token")
    void testGetEnrollmentByToken() {
        when(enrollmentRepository.findByInvitationToken("inv-token-12345")).thenReturn(Optional.of(sampleEnrollment));
        when(enrollmentMapper.toResponse(sampleEnrollment)).thenReturn(sampleEnrollmentResponse);

        EnrollmentResponse result = enrollmentService.getEnrollmentByToken("inv-token-12345");

        assertNotNull(result);
        assertEquals("inv-token-12345", result.getInvitationToken());
    }
}
