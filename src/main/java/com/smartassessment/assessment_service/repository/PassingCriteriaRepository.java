package com.smartassessment.assessment_service.repository;

import com.smartassessment.assessment_service.entity.PassingCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassingCriteriaRepository extends JpaRepository<PassingCriteria, Long> {

    Optional<PassingCriteria> findByAssessmentId(Long assessmentId);
}
