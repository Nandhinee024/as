package com.smartassessment.assessment_service.repository;

import com.smartassessment.assessment_service.entity.AssessmentQuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentQuestionSetRepository extends JpaRepository<AssessmentQuestionSet, Long> {

    List<AssessmentQuestionSet> findBySectionId(Long sectionId);
}
