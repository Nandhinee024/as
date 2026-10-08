package com.smartassessment.assessment.service;

import com.smartassessment.assessment.dto.request.AssessmentSearchCriteria;
import com.smartassessment.assessment.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment.dto.request.PublishAssessmentRequest;
import com.smartassessment.assessment.dto.request.UpdateAssessmentRequest;
import com.smartassessment.assessment.dto.response.*;
import org.springframework.data.domain.Pageable;

public interface AssessmentService {

    AssessmentDetailResponse createAssessment(CreateAssessmentRequest request, String userId);

    AssessmentDetailResponse getAssessmentById(Long id);

    AssessmentDetailResponse getAssessmentByCode(String code);

    PagedResponse<AssessmentSummaryResponse> searchAssessments(AssessmentSearchCriteria criteria, Pageable pageable);

    AssessmentDetailResponse updateAssessment(Long id, UpdateAssessmentRequest request, String userId);

    void deleteAssessment(Long id, String userId);

    AssessmentDetailResponse publishAssessment(Long id, PublishAssessmentRequest request, String userId);

    AssessmentDetailResponse archiveAssessment(Long id, String userId);

    CandidateAssessmentAccessResponse verifyCandidateAccessByToken(String invitationToken);

    boolean verifyAssessmentPasscode(Long assessmentId, String candidateId, String passcode);

    ExamSessionAssessmentConfigResponse getAssessmentConfigForExamSession(Long assessmentId, String candidateId);
}
