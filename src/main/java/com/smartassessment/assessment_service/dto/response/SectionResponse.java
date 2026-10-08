package com.smartassessment.assessment_service.dto.response;

import com.smartassessment.assessment_service.enums.SectionType;
import lombok.Data;

@Data
public class SectionResponse {

    private Long id;
    private String sectionName;
    private SectionType sectionType;
    private Integer marksPerQuestion;
    private Integer totalQuestions;
}
