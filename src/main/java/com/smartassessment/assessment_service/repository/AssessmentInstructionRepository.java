package com.smartassessment.assessment_service.repository;

import com.smartassessment.assessment_service.entity.AssessmentInstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentInstructionRepository extends JpaRepository<AssessmentInstruction, Long> {

    List<AssessmentInstruction> findByAssessmentId(Long assessmentId);
}
