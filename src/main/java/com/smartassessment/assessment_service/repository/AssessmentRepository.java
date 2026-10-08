package com.smartassessment.assessment_service.repository;

import com.smartassessment.assessment_service.entity.Assessment;
import com.smartassessment.assessment_service.enums.AssessmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    List<Assessment> findByStatus(AssessmentStatus status);

    boolean existsByTitleIgnoreCase(String title);
}
