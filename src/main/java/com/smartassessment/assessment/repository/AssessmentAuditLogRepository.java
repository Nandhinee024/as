package com.smartassessment.assessment.repository;

import com.smartassessment.assessment.entity.AssessmentAuditLog;
import com.smartassessment.assessment.entity.AuditAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentAuditLogRepository extends JpaRepository<AssessmentAuditLog, Long> {

    List<AssessmentAuditLog> findByAssessmentIdOrderByTimestampDesc(Long assessmentId);

    Page<AssessmentAuditLog> findByAssessmentId(Long assessmentId, Pageable pageable);

    Page<AssessmentAuditLog> findByAssessmentIdAndAction(Long assessmentId, AuditAction action, Pageable pageable);

    Page<AssessmentAuditLog> findByUserId(String userId, Pageable pageable);
}
