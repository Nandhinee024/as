package com.smartassessment.assessment.service;

import com.smartassessment.assessment.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment.dto.request.PublishAssessmentRequest;
import com.smartassessment.assessment.dto.request.UpdateAssessmentRequest;
import com.smartassessment.assessment.dto.response.AssessmentDetailResponse;
import com.smartassessment.assessment.entity.*;
import com.smartassessment.assessment.exception.BusinessException;
import com.smartassessment.assessment.exception.DuplicateResourceException;
import com.smartassessment.assessment.exception.ResourceNotFoundException;
import com.smartassessment.assessment.mapper.AssessmentMapper;
import com.smartassessment.assessment.repository.AssessmentEnrollmentRepository;
import com.smartassessment.assessment.repository.AssessmentProctoringRuleRepository;
import com.smartassessment.assessment.repository.AssessmentRepository;
import com.smartassessment.assessment.repository.AssessmentSectionRepository;
import com.smartassessment.assessment.service.impl.AssessmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentSectionRepository sectionRepository;

    @Mock
    private AssessmentProctoringRuleRepository proctoringRuleRepository;

    @Mock
    private AssessmentEnrollmentRepository enrollmentRepository;

    @Mock
    private AssessmentMapper assessmentMapper;

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    private CreateAssessmentRequest createRequest;
    private Assessment sampleAssessment;
    private AssessmentDetailResponse sampleResponse;

    @BeforeEach
    void setUp() {
        createRequest = CreateAssessmentRequest.builder()
                .title("Java Senior Software Engineer Assessment")
                .code("JAVA-SR-2026")
                .description("Technical assessment for Java developers")
                .instructions("Autosave is enabled.")
                .category("Software Engineering")
                .accessType(AccessType.INVITE_ONLY)
                .totalDurationMinutes(90)
                .totalMarks(BigDecimal.valueOf(100.00))
                .passingMarks(BigDecimal.valueOf(60.00))
                .passingPercentage(BigDecimal.valueOf(60.00))
                .shuffleQuestions(true)
                .shuffleOptions(true)
                .build();

        sampleAssessment = Assessment.builder()
                .id(1L)
                .title("Java Senior Software Engineer Assessment")
                .code("JAVA-SR-2026")
                .category("Software Engineering")
                .status(AssessmentStatus.DRAFT)
                .accessType(AccessType.INVITE_ONLY)
                .totalDurationMinutes(90)
                .totalMarks(BigDecimal.valueOf(100.00))
                .passingMarks(BigDecimal.valueOf(60.00))
                .passingPercentage(BigDecimal.valueOf(60.00))
                .createdBy("admin-101")
                .isDeleted(false)
                .build();

        sampleResponse = new AssessmentDetailResponse();
        sampleResponse.setId(1L);
        sampleResponse.setTitle("Java Senior Software Engineer Assessment");
        sampleResponse.setCode("JAVA-SR-2026");
        sampleResponse.setStatus(AssessmentStatus.DRAFT);
    }

    @Test
    @DisplayName("Should create assessment successfully and initialize default proctoring rules")
    void testCreateAssessment_Success() {
        when(assessmentRepository.existsByCodeAndIsDeletedFalse("JAVA-SR-2026")).thenReturn(false);
        when(assessmentMapper.toEntity(any(CreateAssessmentRequest.class), eq("admin-101"))).thenReturn(sampleAssessment);
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(sampleAssessment);
        when(assessmentMapper.toDetailResponse(any(Assessment.class))).thenReturn(sampleResponse);

        AssessmentDetailResponse response = assessmentService.createAssessment(createRequest, "admin-101");

        assertNotNull(response);
        assertEquals("JAVA-SR-2026", response.getCode());
        verify(proctoringRuleRepository, times(1)).save(any(AssessmentProctoringRule.class));
        verify(auditLogService, times(1)).logAction(eq(1L), eq("admin-101"), eq("ADMIN"), eq(AuditAction.CREATED), anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when assessment code already exists")
    void testCreateAssessment_DuplicateCode() {
        when(assessmentRepository.existsByCodeAndIsDeletedFalse("JAVA-SR-2026")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> assessmentService.createAssessment(createRequest, "admin-101"));
        verify(assessmentRepository, never()).save(any(Assessment.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when passing marks exceed total marks")
    void testCreateAssessment_PassingMarksExceedTotal() {
        createRequest.setPassingMarks(BigDecimal.valueOf(120.00));
        when(assessmentRepository.existsByCodeAndIsDeletedFalse("JAVA-SR-2026")).thenReturn(false);

        assertThrows(BusinessException.class, () -> assessmentService.createAssessment(createRequest, "admin-101"));
    }

    @Test
    @DisplayName("Should publish assessment when sections and questions are properly configured")
    void testPublishAssessment_Success() {
        AssessmentSection section = AssessmentSection.builder()
                .id(1L)
                .title("MCQ Section")
                .totalQuestionsToAsk(10)
                .build();

        when(assessmentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(sampleAssessment));
        when(sectionRepository.findByAssessmentIdOrderBySectionOrderAsc(1L)).thenReturn(List.of(section));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(sampleAssessment);
        when(assessmentMapper.toDetailResponse(any(Assessment.class))).thenReturn(sampleResponse);

        AssessmentDetailResponse result = assessmentService.publishAssessment(1L, new PublishAssessmentRequest(), "admin-101");

        assertNotNull(result);
        assertEquals(AssessmentStatus.PUBLISHED, sampleAssessment.getStatus());
        verify(auditLogService, times(1)).logAction(eq(1L), eq("admin-101"), eq("ADMIN"), eq(AuditAction.PUBLISHED), anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("Should fail publishing assessment if no sections are present")
    void testPublishAssessment_NoSections_ThrowsException() {
        when(assessmentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(sampleAssessment));
        when(sectionRepository.findByAssessmentIdOrderBySectionOrderAsc(1L)).thenReturn(List.of());

        assertThrows(BusinessException.class, () -> assessmentService.publishAssessment(1L, new PublishAssessmentRequest(), "admin-101"));
    }

    @Test
    @DisplayName("Should soft delete assessment and record audit log")
    void testDeleteAssessment_Success() {
        when(assessmentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(sampleAssessment));
        when(assessmentMapper.toDetailResponse(sampleAssessment)).thenReturn(sampleResponse);

        assessmentService.deleteAssessment(1L, "admin-101");

        assertTrue(sampleAssessment.getIsDeleted());
        assertEquals(AssessmentStatus.ARCHIVED, sampleAssessment.getStatus());
        verify(assessmentRepository, times(1)).save(sampleAssessment);
        verify(auditLogService, times(1)).logAction(eq(1L), eq("admin-101"), eq("ADMIN"), eq(AuditAction.ARCHIVED), anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when assessment ID is invalid")
    void testGetAssessmentById_NotFound() {
        when(assessmentRepository.findByIdAndIsDeletedFalse(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> assessmentService.getAssessmentById(999L));
    }
}
