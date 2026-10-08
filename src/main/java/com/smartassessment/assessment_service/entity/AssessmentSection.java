package com.smartassessment.assessment_service.entity;

import com.smartassessment.assessment_service.enums.SectionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessment_sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "section_type")
    private SectionType sectionType;

    @Column(name = "marks_per_question")
    private Integer marksPerQuestion;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    // One section -> many question sets
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssessmentQuestionSet> questionSets = new ArrayList<>();
}
