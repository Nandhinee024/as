package com.smartassessment.assessment.mapper;

import com.smartassessment.assessment.dto.response.SectionResponse;
import com.smartassessment.assessment.entity.AssessmentSection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SectionMapper {

    public SectionResponse toResponse(AssessmentSection section) {
        if (section == null) {
            return null;
        }
        return SectionResponse.builder()
                .id(section.getId())
                .assessmentId(section.getAssessment() != null ? section.getAssessment().getId() : null)
                .title(section.getTitle())
                .description(section.getDescription())
                .sectionOrder(section.getSectionOrder())
                .sectionDurationMinutes(section.getSectionDurationMinutes())
                .cutoffMarks(section.getCutoffMarks())
                .weightagePercentage(section.getWeightagePercentage())
                .questionType(section.getQuestionType())
                .totalQuestionsToAsk(section.getTotalQuestionsToAsk())
                .easyQuestionCount(section.getEasyQuestionCount())
                .mediumQuestionCount(section.getMediumQuestionCount())
                .hardQuestionCount(section.getHardQuestionCount())
                .questionSetId(section.getQuestionSetId())
                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .build();
    }

    public List<SectionResponse> toResponseList(List<AssessmentSection> sections) {
        if (sections == null) {
            return List.of();
        }
        return sections.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
