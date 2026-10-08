package com.smartassessment.assessment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "passing_criteria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassingCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false, unique = true)
    private Assessment assessment;

    @Column(name = "passing_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal passingPercentage;

    @Column(name = "passing_marks")
    private Integer passingMarks;
}
