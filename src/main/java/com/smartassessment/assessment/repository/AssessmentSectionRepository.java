package com.smartassessment.assessment.repository;

import com.smartassessment.assessment.entity.AssessmentSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentSectionRepository extends JpaRepository<AssessmentSection, Long> {

    List<AssessmentSection> findByAssessmentIdOrderBySectionOrderAsc(Long assessmentId);

    Optional<AssessmentSection> findByIdAndAssessmentId(Long id, Long assessmentId);

    void deleteByAssessmentId(Long assessmentId);

    @Query("SELECT COALESCE(MAX(s.sectionOrder), 0) FROM AssessmentSection s WHERE s.assessment.id = :assessmentId")
    int findMaxSectionOrderByAssessmentId(@Param("assessmentId") Long assessmentId);

    long countByAssessmentId(Long assessmentId);
}
