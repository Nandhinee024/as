package com.smartassessment.assessment_service.repository;

import com.smartassessment.assessment_service.entity.ProctoringRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProctoringRuleRepository extends JpaRepository<ProctoringRule, Long> {

    Optional<ProctoringRule> findByAssessmentId(Long assessmentId);
}
