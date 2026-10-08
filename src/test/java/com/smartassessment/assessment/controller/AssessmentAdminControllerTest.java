package com.smartassessment.assessment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartassessment.assessment.config.security.JwtAuthenticationFilter;
import com.smartassessment.assessment.config.security.JwtTokenProvider;
import com.smartassessment.assessment.config.security.SecurityConfig;
import com.smartassessment.assessment.dto.request.CreateAssessmentRequest;
import com.smartassessment.assessment.dto.response.AssessmentDetailResponse;
import com.smartassessment.assessment.entity.AccessType;
import com.smartassessment.assessment.entity.AssessmentStatus;
import com.smartassessment.assessment.service.AssessmentService;
import com.smartassessment.assessment.service.AuditLogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssessmentAdminController.class)
@Import(SecurityConfig.class)
class AssessmentAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssessmentService assessmentService;

    @MockBean
    private AuditLogService auditLogService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin should create assessment successfully")
    void testCreateAssessment_Success() throws Exception {
        CreateAssessmentRequest request = CreateAssessmentRequest.builder()
                .title("Full Stack Java Assessment")
                .code("JAVA-FULLSTACK-2026")
                .category("Software Engineering")
                .accessType(AccessType.INVITE_ONLY)
                .totalDurationMinutes(60)
                .totalMarks(BigDecimal.valueOf(100.00))
                .passingMarks(BigDecimal.valueOf(50.00))
                .build();

        AssessmentDetailResponse response = new AssessmentDetailResponse();
        response.setId(1L);
        response.setTitle("Full Stack Java Assessment");
        response.setCode("JAVA-FULLSTACK-2026");
        response.setStatus(AssessmentStatus.DRAFT);

        when(assessmentService.createAssessment(any(CreateAssessmentRequest.class), any())).thenReturn(response);

        mockMvc.perform(post("/api/assessments/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.code").value("JAVA-FULLSTACK-2026"));
    }

    @Test
    @WithMockUser(roles = "CANDIDATE")
    @DisplayName("Candidate user should be forbidden (403) from accessing admin endpoints")
    void testAdminEndpoint_ForbiddenForCandidate() throws Exception {
        mockMvc.perform(get("/api/assessments/admin/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Unauthenticated request should return 401 Unauthorized")
    void testAdminEndpoint_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/assessments/admin/1"))
                .andExpect(status().isUnauthorized());
    }
}
