package com.smartassessment.assessment.service;

import com.smartassessment.assessment.dto.request.BulkEnrollCandidatesRequest;
import com.smartassessment.assessment.dto.request.EnrollCandidateRequest;
import com.smartassessment.assessment.dto.response.EnrollmentResponse;
import com.smartassessment.assessment.dto.response.PagedResponse;
import com.smartassessment.assessment.entity.EnrollmentStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponse enrollCandidate(Long assessmentId, EnrollCandidateRequest request, String userId);

    List<EnrollmentResponse> bulkEnrollCandidates(Long assessmentId, BulkEnrollCandidatesRequest request, String userId);

    PagedResponse<EnrollmentResponse> getEnrollmentsByAssessment(Long assessmentId, EnrollmentStatus status, Pageable pageable);

    EnrollmentResponse getEnrollmentByToken(String token);

    List<EnrollmentResponse> getEnrollmentsByCandidateId(String candidateId);

    EnrollmentResponse updateEnrollmentStatus(Long assessmentId, Long enrollmentId, EnrollmentStatus newStatus, String userId);

    void cancelEnrollment(Long assessmentId, Long enrollmentId, String userId);
}
