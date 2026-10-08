package com.smartassessment.assessment.exception;

import com.smartassessment.assessment.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/assessments/123");
    }

    @Test
    @DisplayName("Should return 404 with standard error body for ResourceNotFoundException")
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Assessment not found");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleResourceNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().getError());
        assertEquals("Assessment not found", response.getBody().getMessage());
        assertEquals("/api/assessments/123", response.getBody().getPath());
        assertNotNull(response.getBody().getTraceId());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should return 409 for DuplicateResourceException")
    void testHandleDuplicateResourceException() {
        DuplicateResourceException ex = new DuplicateResourceException("Code already exists");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleDuplicateResourceException(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("DUPLICATE_RESOURCE", response.getBody().getError());
    }

    @Test
    @DisplayName("Should return 422 for BusinessException")
    void testHandleBusinessException() {
        BusinessException ex = new BusinessException("Cannot publish empty assessment");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleBusinessException(ex, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(422, response.getBody().getStatus());
        assertEquals("BUSINESS_RULE_VIOLATION", response.getBody().getError());
    }
}
