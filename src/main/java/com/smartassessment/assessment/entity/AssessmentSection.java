package com.smartassessment.assessment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_sections", indexes = {
        @Index(name = "idx_sections_assessment_id", columnList = "assessment_id"),
        @Index(name = "idx_sections_order", columnList = "assessment_id, section_order")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AssessmentSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "section_order", nullable = false)
    @Builder.Default
    private Integer sectionOrder = 1;

    @Column(name = "section_duration_minutes")
    private Integer sectionDurationMinutes;

    @Column(name = "cutoff_marks", precision = 10, scale = 2)
    private BigDecimal cutoffMarks;

    @Column(name = "weightage_percentage", precision = 5, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal weightagePercentage = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false, length = 50)
    @Builder.Default
    private QuestionType questionType = QuestionType.MCQ;

    @Column(name = "total_questions_to_ask", nullable = false)
    @Builder.Default
    private Integer totalQuestionsToAsk = 10;

    @Column(name = "easy_question_count", nullable = false)
    @Builder.Default
    private Integer easyQuestionCount = 0;

    @Column(name = "medium_question_count", nullable = false)
    @Builder.Default
    private Integer mediumQuestionCount = 0;

    @Column(name = "hard_question_count", nullable = false)
    @Builder.Default
    private Integer hardQuestionCount = 0;

    @Column(name = "question_set_id", length = 100)
    private String questionSetId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
