package com.smartassessment.assessment_service.service.impl;

import com.smartassessment.assessment_service.dto.request.CreateSectionRequest;
import com.smartassessment.assessment_service.dto.request.QuestionDistributionRequest;
import com.smartassessment.assessment_service.dto.request.UpdateSectionRequest;
import com.smartassessment.assessment_service.dto.response.SectionResponse;
import com.smartassessment.assessment_service.entity.Assessment;
import com.smartassessment.assessment_service.entity.AssessmentQuestionSet;
import com.smartassessment.assessment_service.entity.AssessmentSection;
import com.smartassessment.assessment_service.exception.ResourceNotFoundException;
import com.smartassessment.assessment_service.repository.AssessmentRepository;
import com.smartassessment.assessment_service.repository.AssessmentSectionRepository;
import com.smartassessment.assessment_service.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectionServiceImpl implements SectionService {

    private final AssessmentSectionRepository sectionRepository;
    private final AssessmentRepository assessmentRepository;

    @Override
    @Transactional
    public SectionResponse createSection(CreateSectionRequest request) {
        log.info("Creating section for assessment id: {}", request.getAssessmentId());

        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + request.getAssessmentId()));

        AssessmentSection section = new AssessmentSection();
        section.setAssessment(assessment);
        section.setSectionName(request.getSectionName());
        section.setSectionType(request.getSectionType());
        section.setMarksPerQuestion(request.getMarksPerQuestion());

        // Add question distributions if provided
        if (request.getQuestionDistributions() != null) {
            int total = 0;
            for (QuestionDistributionRequest dist : request.getQuestionDistributions()) {
                AssessmentQuestionSet qSet = new AssessmentQuestionSet();
                qSet.setSection(section);
                qSet.setTopicId(dist.getTopicId());
                qSet.setNumQuestions(dist.getNumQuestions());
                qSet.setDifficulty(dist.getDifficulty());
                qSet.setDistributionType(dist.getDistributionType());
                section.getQuestionSets().add(qSet);
                total += dist.getNumQuestions();
            }
            section.setTotalQuestions(total);
        }

        AssessmentSection saved = sectionRepository.save(section);
        log.info("Section created with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public SectionResponse getSectionById(Long id) {
        log.info("Fetching section by id: {}", id);
        return mapToResponse(findById(id));
    }

    @Override
    public List<SectionResponse> getSectionsByAssessmentId(Long assessmentId) {
        log.info("Fetching sections for assessment id: {}", assessmentId);
        return sectionRepository.findByAssessmentId(assessmentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SectionResponse updateSection(Long id, UpdateSectionRequest request) {
        log.info("Updating section with id: {}", id);

        AssessmentSection section = findById(id);
        section.setSectionName(request.getSectionName());
        section.setSectionType(request.getSectionType());
        section.setMarksPerQuestion(request.getMarksPerQuestion());

        AssessmentSection updated = sectionRepository.save(section);
        log.info("Section updated with id: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSection(Long id) {
        log.info("Deleting section with id: {}", id);
        if (!sectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Section not found with id: " + id);
        }
        sectionRepository.deleteById(id);
        log.info("Section deleted with id: {}", id);
    }

    // ---- Private Helpers ----

    private AssessmentSection findById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));
    }

    private SectionResponse mapToResponse(AssessmentSection section) {
        SectionResponse response = new SectionResponse();
        response.setId(section.getId());
        response.setSectionName(section.getSectionName());
        response.setSectionType(section.getSectionType());
        response.setMarksPerQuestion(section.getMarksPerQuestion());
        response.setTotalQuestions(section.getTotalQuestions());
        return response;
    }
}
