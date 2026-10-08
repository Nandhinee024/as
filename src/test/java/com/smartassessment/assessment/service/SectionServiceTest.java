package com.smartassessment.assessment.service;

import com.smartassessment.assessment.client.QuestionBankClient;
import com.smartassessment.assessment.dto.request.CreateSectionRequest;
import com.smartassessment.assessment.dto.response.SectionResponse;
import com.smartassessment.assessment.entity.Assessment;
import com.smartassessment.assessment.entity.AssessmentSection;
import com.smartassessment.assessment.entity.AssessmentStatus;
import com.smartassessment.assessment.entity.QuestionType;
import com.smartassessment.assessment.exception.BusinessException;
import com.smartassessment.assessment.mapper.SectionMapper;
import com.smartassessment.assessment.repository.AssessmentRepository;
import com.smartassessment.assessment.repository.AssessmentSectionRepository;
import com.smartassessment.assessment.service.impl.SectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentSectionRepository sectionRepository;

    @Mock
    private SectionMapper sectionMapper;

    @Mock
    private QuestionBankClient questionBankClient;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private SectionServiceImpl sectionService;

    private Assessment sampleAssessment;
    private AssessmentSection sampleSection;
    private SectionResponse sampleSectionResponse;

    @BeforeEach
    void setUp() {
        sampleAssessment = Assessment.builder()
                .id(1L)
                .title("Java Assessment")
                .status(AssessmentStatus.DRAFT)
                .isDeleted(false)
                .build();

        sampleSection = AssessmentSection.builder()
                .id(10L)
                .assessment(sampleAssessment)
                .title("MCQ Section")
                .sectionOrder(1)
                .questionType(QuestionType.MCQ)
                .totalQuestionsToAsk(10)
                .easyQuestionCount(3)
                .mediumQuestionCount(5)
                .hardQuestionCount(2)
                .weightagePercentage(BigDecimal.valueOf(30.00))
                .build();

        sampleSectionResponse = SectionResponse.builder()
                .id(10L)
                .assessmentId(1L)
                .title("MCQ Section")
                .sectionOrder(1)
                .questionType(QuestionType.MCQ)
                .totalQuestionsToAsk(10)
                .build();
    }

    @Test
    @DisplayName("Should add section successfully to assessment")
    void testAddSection_Success() {
        CreateSectionRequest request = CreateSectionRequest.builder()
                .title("MCQ Section")
                .questionType(QuestionType.MCQ)
                .totalQuestionsToAsk(10)
                .easyQuestionCount(3)
                .mediumQuestionCount(5)
                .hardQuestionCount(2)
                .weightagePercentage(BigDecimal.valueOf(30.00))
                .build();

        when(assessmentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(sampleAssessment));
        when(sectionRepository.findMaxSectionOrderByAssessmentId(1L)).thenReturn(0);
        when(sectionRepository.save(any(AssessmentSection.class))).thenReturn(sampleSection);
        when(sectionMapper.toResponse(any(AssessmentSection.class))).thenReturn(sampleSectionResponse);

        SectionResponse result = sectionService.addSection(1L, request, "admin-101");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(sectionRepository, times(1)).save(any(AssessmentSection.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when difficulty distribution sum does not match total questions")
    void testAddSection_MismatchDifficultyCount() {
        CreateSectionRequest request = CreateSectionRequest.builder()
                .title("MCQ Section")
                .questionType(QuestionType.MCQ)
                .totalQuestionsToAsk(10)
                .easyQuestionCount(2)
                .mediumQuestionCount(2)
                .hardQuestionCount(2) // Total = 6 != 10
                .weightagePercentage(BigDecimal.valueOf(30.00))
                .build();

        when(assessmentRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(sampleAssessment));

        assertThrows(BusinessException.class, () -> sectionService.addSection(1L, request, "admin-101"));
        verify(sectionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve sections in execution order")
    void testGetSectionsByAssessmentId() {
        when(assessmentRepository.existsById(1L)).thenReturn(true);
        when(sectionRepository.findByAssessmentIdOrderBySectionOrderAsc(1L)).thenReturn(List.of(sampleSection));
        when(sectionMapper.toResponseList(anyList())).thenReturn(List.of(sampleSectionResponse));

        List<SectionResponse> result = sectionService.getSectionsByAssessmentId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
