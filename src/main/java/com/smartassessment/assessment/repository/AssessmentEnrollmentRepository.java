package com.smartassessment.assessment.repository;

import com.smartassessment.assessment.entity.AssessmentEnrollment;
import com.smartassessment.assessment.entity.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentEnrollmentRepository extends JpaRepository<AssessmentEnrollment, Long> {

    Optional<AssessmentEnrollment> findByAssessmentIdAndCandidateId(Long assessmentId, String candidateId);

    Optional<AssessmentEnrollment> findByInvitationToken(String invitationToken);

    List<AssessmentEnrollment> findByCandidateId(String candidateId);

    Page<AssessmentEnrollment> findByAssessmentId(Long assessmentId, Pageable pageable);

    Page<AssessmentEnrollment> findByAssessmentIdAndEnrollmentStatus(Long assessmentId, EnrollmentStatus status, Pageable pageable);

    boolean existsByAssessmentIdAndCandidateId(Long assessmentId, String candidateId);

    long countByAssessmentId(Long assessmentId);

    long countByAssessmentIdAndEnrollmentStatus(Long assessmentId, EnrollmentStatus status);
}
