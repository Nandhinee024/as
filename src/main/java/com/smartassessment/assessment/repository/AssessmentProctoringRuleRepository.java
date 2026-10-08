package com.smartassessment.assessment.repository;

import com.smartassessment.assessment.entity.AssessmentProctoringRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssessmentProctoringRuleRepository extends JpaRepository<AssessmentProctoringRule, Long> {

    Optional<AssessmentProctoringRule> findByAssessmentId(Long assessmentId);

    void deleteByAssessmentId(Long assessmentId);
}
