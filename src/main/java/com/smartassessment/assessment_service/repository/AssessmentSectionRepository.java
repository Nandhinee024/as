package com.smartassessment.assessment_service.repository;

import com.smartassessment.assessment_service.entity.AssessmentSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentSectionRepository extends JpaRepository<AssessmentSection, Long> {

    List<AssessmentSection> findByAssessmentId(Long assessmentId);
}
