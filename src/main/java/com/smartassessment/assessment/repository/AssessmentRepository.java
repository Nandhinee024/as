package com.smartassessment.assessment.repository;

import com.smartassessment.assessment.entity.Assessment;
import com.smartassessment.assessment.entity.AssessmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long>, JpaSpecificationExecutor<Assessment> {

    Optional<Assessment> findByIdAndIsDeletedFalse(Long id);

    Optional<Assessment> findByCodeAndIsDeletedFalse(String code);

    boolean existsByCodeAndIsDeletedFalse(String code);

    boolean existsByCodeAndIdNotAndIsDeletedFalse(String code, Long id);

    Page<Assessment> findByIsDeletedFalse(Pageable pageable);

    Page<Assessment> findByStatusAndIsDeletedFalse(AssessmentStatus status, Pageable pageable);

    Page<Assessment> findByCategoryAndIsDeletedFalse(String category, Pageable pageable);

    Page<Assessment> findByCreatedByAndIsDeletedFalse(String createdBy, Pageable pageable);

    @Query("SELECT a FROM Assessment a WHERE a.isDeleted = false " +
            "AND a.status = :status " +
            "AND (a.validFrom IS NULL OR a.validFrom <= :now) " +
            "AND (a.validTo IS NULL OR a.validTo >= :now)")
    List<Assessment> findActiveAssessments(@Param("status") AssessmentStatus status, @Param("now") LocalDateTime now);
}
