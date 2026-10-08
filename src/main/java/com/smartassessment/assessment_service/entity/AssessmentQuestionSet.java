package com.smartassessment.assessment_service.entity;

import com.smartassessment.assessment_service.enums.AssessmentDifficulty;
import com.smartassessment.assessment_service.enums.DistributionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assessment_question_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentQuestionSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private AssessmentSection section;

    // References topic from question-bank service (external ID)
    @Column(name = "topic_id")
    private Long topicId;

    @Enumerated(EnumType.STRING)
    private AssessmentDifficulty difficulty;

    @Column(name = "num_questions", nullable = false)
    private Integer numQuestions;

    @Enumerated(EnumType.STRING)
    @Column(name = "distribution_type")
    private DistributionType distributionType;
}
