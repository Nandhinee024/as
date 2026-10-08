package com.smartassessment.assessment.mapper;

import com.smartassessment.assessment.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment.dto.response.AssessmentDetailResponse;
import com.smartassessment.assessment.dto.response.AssessmentResponse;
import com.smartassessment.assessment.dto.response.AssessmentSummaryResponse;
import com.smartassessment.assessment.dto.response.ExamSessionAssessmentConfigResponse;
import com.smartassessment.assessment.entity.Assessment;
import com.smartassessment.assessment.entity.AssessmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssessmentMapper {

    private final SectionMapper sectionMapper;
    private final ProctoringRuleMapper proctoringRuleMapper;

    public Assessment toEntity(CreateAssessmentRequest request, String createdBy) {
        if (request == null) {
            return null;
        }

        BigDecimal passingPercentage = request.getPassingPercentage();
        if (passingPercentage == null && request.getTotalMarks() != null && request.getPassingMarks() != null
                && request.getTotalMarks().compareTo(BigDecimal.ZERO) > 0) {
            passingPercentage = request.getPassingMarks()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(request.getTotalMarks(), 2, RoundingMode.HALF_UP);
        }

        return Assessment.builder()
                .title(request.getTitle())
                .code(request.getCode().toUpperCase().trim())
                .description(request.getDescription())
                .instructions(request.getInstructions())
                .category(request.getCategory())
                .status(AssessmentStatus.DRAFT)
                .accessType(request.getAccessType())
                .passcode(request.getPasscode())
                .totalDurationMinutes(request.getTotalDurationMinutes())
                .totalMarks(request.getTotalMarks())
                .passingMarks(request.getPassingMarks())
                .passingPercentage(passingPercentage != null ? passingPercentage : BigDecimal.valueOf(40.00))
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .shuffleQuestions(request.getShuffleQuestions())
                .shuffleOptions(request.getShuffleOptions())
                .allowNavigationBack(request.getAllowNavigationBack())
                .allowReview(request.getAllowReview())
                .allowRetake(request.getAllowRetake())
                .maxRetakeAttempts(request.getMaxRetakeAttempts())
                .autoSubmitOnExpiry(request.getAutoSubmitOnExpiry())
                .connectionGracePeriodMinutes(request.getConnectionGracePeriodMinutes())
                .dynamicQuestionnaireEnabled(request.getDynamicQuestionnaireEnabled())
                .showResultsImmediately(request.getShowResultsImmediately())
                .createdBy(createdBy)
                .isDeleted(false)
                .build();
    }

    public AssessmentResponse toResponse(Assessment assessment) {
        if (assessment == null) {
            return null;
        }
        return AssessmentResponse.builder()
                .id(assessment.getId())
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
                .passingPercentage(assessment.getPassingPercentage())
                .validFrom(assessment.getValidFrom())
                .validTo(assessment.getValidTo())
                .shuffleQuestions(assessment.getShuffleQuestions())
                .shuffleOptions(assessment.getShuffleOptions())
                .allowNavigationBack(assessment.getAllowNavigationBack())
                .allowReview(assessment.getAllowReview())
                .allowRetake(assessment.getAllowRetake())
                .maxRetakeAttempts(assessment.getMaxRetakeAttempts())
                .autoSubmitOnExpiry(assessment.getAutoSubmitOnExpiry())
                .connectionGracePeriodMinutes(assessment.getConnectionGracePeriodMinutes())
                .dynamicQuestionnaireEnabled(assessment.getDynamicQuestionnaireEnabled())
                .showResultsImmediately(assessment.getShowResultsImmediately())
                .createdBy(assessment.getCreatedBy())
                .updatedBy(assessment.getUpdatedBy())
                .createdAt(assessment.getCreatedAt())
                .updatedAt(assessment.getUpdatedAt())
                .build();
    }

    public AssessmentSummaryResponse toSummaryResponse(Assessment assessment) {
        if (assessment == null) {
            return null;
        }
        return AssessmentSummaryResponse.builder()
                .id(assessment.getId())
                .title(assessment.getTitle())
                .code(assessment.getCode())
                .category(assessment.getCategory())
                .status(assessment.getStatus())
                .accessType(assessment.getAccessType())
                .totalDurationMinutes(assessment.getTotalDurationMinutes())
                .totalMarks(assessment.getTotalMarks())
                .passingMarks(assessment.getPassingMarks())
                .sectionCount(assessment.getSections() != null ? assessment.getSections().size() : 0)
                .enrollmentCount(assessment.getEnrollments() != null ? assessment.getEnrollments().size() : 0)
                .validFrom(assessment.getValidFrom())
                .validTo(assessment.getValidTo())
                .createdBy(assessment.getCreatedBy())
                .createdAt(assessment.getCreatedAt())
                .build();
    }

    public AssessmentDetailResponse toDetailResponse(Assessment assessment) {
        if (assessment == null) {
            return null;
        }

        AssessmentDetailResponse response = new AssessmentDetailResponse();
        response.setId(assessment.getId());
        response.setTitle(assessment.getTitle());
        response.setCode(assessment.getCode());
        response.setDescription(assessment.getDescription());
        response.setInstructions(assessment.getInstructions());
        response.setCategory(assessment.getCategory());
        response.setStatus(assessment.getStatus());
        response.setAccessType(assessment.getAccessType());
        response.setTotalDurationMinutes(assessment.getTotalDurationMinutes());
        response.setTotalMarks(assessment.getTotalMarks());
        response.setPassingMarks(assessment.getPassingMarks());
        response.setPassingPercentage(assessment.getPassingPercentage());
        response.setValidFrom(assessment.getValidFrom());
        response.setValidTo(assessment.getValidTo());
        response.setShuffleQuestions(assessment.getShuffleQuestions());
        response.setShuffleOptions(assessment.getShuffleOptions());
        response.setAllowNavigationBack(assessment.getAllowNavigationBack());
        response.setAllowReview(assessment.getAllowReview());
        response.setAllowRetake(assessment.getAllowRetake());
        response.setMaxRetakeAttempts(assessment.getMaxRetakeAttempts());
        response.setAutoSubmitOnExpiry(assessment.getAutoSubmitOnExpiry());
        response.setConnectionGracePeriodMinutes(assessment.getConnectionGracePeriodMinutes());
        response.setDynamicQuestionnaireEnabled(assessment.getDynamicQuestionnaireEnabled());
        response.setShowResultsImmediately(assessment.getShowResultsImmediately());
        response.setCreatedBy(assessment.getCreatedBy());
        response.setUpdatedBy(assessment.getUpdatedBy());
        response.setCreatedAt(assessment.getCreatedAt());
        response.setUpdatedAt(assessment.getUpdatedAt());

        response.setSections(sectionMapper.toResponseList(assessment.getSections()));
        response.setProctoringRule(proctoringRuleMapper.toResponse(assessment.getProctoringRule()));
        
        int totalQuestions = assessment.getSections() != null
                ? assessment.getSections().stream().mapToInt(s -> s.getTotalQuestionsToAsk() != null ? s.getTotalQuestionsToAsk() : 0).sum()
                : 0;
        response.setTotalQuestionsConfigured(totalQuestions);

        return response;
    }

    public ExamSessionAssessmentConfigResponse toExamSessionConfig(Assessment assessment) {
        if (assessment == null) {
            return null;
        }
        return ExamSessionAssessmentConfigResponse.builder()
                .assessmentId(assessment.getId())
                .code(assessment.getCode())
                .title(assessment.getTitle())
                .status(assessment.getStatus())
                .accessType(assessment.getAccessType())
                .totalDurationMinutes(assessment.getTotalDurationMinutes())
                .totalMarks(assessment.getTotalMarks())
                .passingMarks(assessment.getPassingMarks())
                .passingPercentage(assessment.getPassingPercentage())
                .shuffleQuestions(assessment.getShuffleQuestions())
                .shuffleOptions(assessment.getShuffleOptions())
                .allowNavigationBack(assessment.getAllowNavigationBack())
                .allowReview(assessment.getAllowReview())
                .allowRetake(assessment.getAllowRetake())
                .maxRetakeAttempts(assessment.getMaxRetakeAttempts())
                .autoSubmitOnExpiry(assessment.getAutoSubmitOnExpiry())
                .connectionGracePeriodMinutes(assessment.getConnectionGracePeriodMinutes())
                .dynamicQuestionnaireEnabled(assessment.getDynamicQuestionnaireEnabled())
                .showResultsImmediately(assessment.getShowResultsImmediately())
                .validFrom(assessment.getValidFrom())
                .validTo(assessment.getValidTo())
                .sections(sectionMapper.toResponseList(assessment.getSections()))
                .proctoringRule(proctoringRuleMapper.toResponse(assessment.getProctoringRule()))
                .build();
    }

    public List<AssessmentSummaryResponse> toSummaryResponseList(List<Assessment> assessments) {
        if (assessments == null) {
            return List.of();
        }
        return assessments.stream().map(this::toSummaryResponse).collect(Collectors.toList());
    }
}
