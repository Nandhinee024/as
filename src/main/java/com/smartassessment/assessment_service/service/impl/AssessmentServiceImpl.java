package com.smartassessment.assessment_service.service.impl;

import com.smartassessment.assessment_service.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment_service.dto.request.UpdateAssessmentRequest;
import com.smartassessment.assessment_service.dto.response.AssessmentResponse;
import com.smartassessment.assessment_service.dto.response.SectionResponse;
import com.smartassessment.assessment_service.entity.Assessment;
import com.smartassessment.assessment_service.entity.AssessmentInstruction;
import com.smartassessment.assessment_service.exception.ResourceNotFoundException;
import com.smartassessment.assessment_service.repository.AssessmentRepository;
import com.smartassessment.assessment_service.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;

    @Override
    @Transactional
    public AssessmentResponse createAssessment(CreateAssessmentRequest request) {
        log.info("Creating new assessment with title: {}", request.getTitle());

        Assessment assessment = new Assessment();
        assessment.setTitle(request.getTitle());
        assessment.setDescription(request.getDescription());
        assessment.setDurationMinutes(request.getDurationMinutes());
        assessment.setTotalMarks(request.getTotalMarks());
        assessment.setDifficulty(request.getDifficulty());

        // Add instructions if provided
        if (request.getInstructions() != null && !request.getInstructions().isEmpty()) {
            for (String text : request.getInstructions()) {
                AssessmentInstruction instruction = new AssessmentInstruction();
                instruction.setAssessment(assessment);
                instruction.setInstruction(text);
                assessment.getInstructions().add(instruction);
            }
        }

        Assessment saved = assessmentRepository.save(assessment);
        log.info("Assessment created successfully with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public AssessmentResponse getAssessmentById(Long id) {
        log.info("Fetching assessment by id: {}", id);
        Assessment assessment = findById(id);
        return mapToResponse(assessment);
    }

    @Override
    public List<AssessmentResponse> getAllAssessments() {
        log.info("Fetching all assessments");
        return assessmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssessmentResponse updateAssessment(Long id, UpdateAssessmentRequest request) {
        log.info("Updating assessment with id: {}", id);

        Assessment assessment = findById(id);
        assessment.setTitle(request.getTitle());
        assessment.setDescription(request.getDescription());
        assessment.setDurationMinutes(request.getDurationMinutes());
        assessment.setTotalMarks(request.getTotalMarks());
        assessment.setDifficulty(request.getDifficulty());

        Assessment updated = assessmentRepository.save(assessment);
        log.info("Assessment updated successfully with id: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteAssessment(Long id) {
        log.info("Deleting assessment with id: {}", id);
        if (!assessmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assessment not found with id: " + id);
        }
        assessmentRepository.deleteById(id);
        log.info("Assessment deleted successfully with id: {}", id);
    }

    // ---- Private Helpers ----

    private Assessment findById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));
    }

    private AssessmentResponse mapToResponse(Assessment assessment) {
        AssessmentResponse response = new AssessmentResponse();
        response.setId(assessment.getId());
        response.setTitle(assessment.getTitle());
        response.setDescription(assessment.getDescription());
        response.setDurationMinutes(assessment.getDurationMinutes());
        response.setTotalMarks(assessment.getTotalMarks());
        response.setStatus(assessment.getStatus());
        response.setDifficulty(assessment.getDifficulty());
        response.setCreatedAt(assessment.getCreatedAt());
        response.setUpdatedAt(assessment.getUpdatedAt());

        // Map instructions (list of strings)
        if (assessment.getInstructions() != null) {
            List<String> instructions = assessment.getInstructions()
                    .stream()
                    .map(AssessmentInstruction::getInstruction)
                    .collect(Collectors.toList());
            response.setInstructions(instructions);
        }

        // Map sections
        if (assessment.getSections() != null) {
            List<SectionResponse> sections = assessment.getSections()
                    .stream()
                    .map(section -> {
                        SectionResponse sr = new SectionResponse();
                        sr.setId(section.getId());
                        sr.setSectionName(section.getSectionName());
                        sr.setSectionType(section.getSectionType());
                        sr.setMarksPerQuestion(section.getMarksPerQuestion());
                        sr.setTotalQuestions(section.getTotalQuestions());
                        return sr;
                    })
                    .collect(Collectors.toList());
            response.setSections(sections);
        }

        return response;
    }
}
