package com.smartassessment.assessment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API Error Response payload")
public class ApiErrorResponse {

    @Schema(description = "Error timestamp in ISO format", example = "2026-09-22T10:20:00Z")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP Status code", example = "404")
    private Integer status;

    @Schema(description = "Standard Error code identifier", example = "RESOURCE_NOT_FOUND")
    private String error;

    @Schema(description = "Human-readable error description", example = "Assessment not found")
    private String message;

    @Schema(description = "Request URI path", example = "/api/assessments/123")
    private String path;

    @Schema(description = "Unique Distributed Trace ID / Correlation ID", example = "abc-123-xyz")
    private String traceId;

    @Schema(description = "Detailed field validation errors if applicable")
    private List<String> details;
}
