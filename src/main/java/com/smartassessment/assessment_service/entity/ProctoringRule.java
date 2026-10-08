package com.smartassessment.assessment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proctoring_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProctoringRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false, unique = true)
    private Assessment assessment;

    @Column(name = "is_camera_required")
    private Boolean isCameraRequired = false;

    @Column(name = "is_screen_share")
    private Boolean isScreenShare = false;

    @Column(name = "max_tab_switches")
    private Integer maxTabSwitches = 0;
}
